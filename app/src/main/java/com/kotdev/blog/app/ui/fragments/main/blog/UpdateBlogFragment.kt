package com.kotdev.blog.app.ui.fragments.main.blog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.kotdev.blog.R
import com.kotdev.blog.app.ui.fragments.main.blog.state.BLOG_VIEW_STATE_BUNDLE_KEY
import com.kotdev.blog.app.ui.fragments.main.blog.state.BlogStateEvent
import com.kotdev.blog.app.ui.fragments.main.blog.state.BlogViewState
import com.kotdev.blog.app.ui.viewmodels.main.blog_viewmodel.*
import com.kotdev.blog.databinding.FragmentUpdateBlogBinding
import com.kotdev.blog.helpers.util.Constants.Companion.GALLERY_REQUEST_CODE
import com.kotdev.blog.app.repository.ErrorHandling.Companion.SOMETHING_WRONG_WITH_IMAGE
import com.kotdev.blog.app.repository.MessageType
import com.kotdev.blog.app.repository.Response
import com.kotdev.blog.app.repository.StateMessageCallback
import com.kotdev.blog.app.repository.SuccessHandling.Companion.SUCCESS_BLOG_UPDATED
import com.kotdev.blog.app.repository.UIComponentType
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class UpdateBlogFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager,
) : BaseBlogFragment(R.layout.fragment_update_blog, viewModelFactory) {

    private var _binding: FragmentUpdateBlogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpdateBlogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state after process death
        savedInstanceState?.let { inState ->
            (inState[BLOG_VIEW_STATE_BUNDLE_KEY] as BlogViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    /**
     * !IMPORTANT!
     * Must save ViewState b/c in event of process death the LiveData in ViewModel will be lost
     */
    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        //clear the list. Don't want to save a large list to bundle.
        viewState?.blogFields?.blogList = ArrayList()

        outState.putParcelable(
            BLOG_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()

        binding.imageContainer.setOnClickListener {
            if (uiCommunicationListener.isStoragePermissionGranted()) {
                pickFromGallery()
            }
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun launchImageCrop(uri: Uri) {
        context?.let {
            CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }
    }

    private fun showImageSelectionError() {
        uiCommunicationListener.onResponseReceived(
            response = Response(
                message = SOMETHING_WRONG_WITH_IMAGE,
                uiComponentType = UIComponentType.Dialog(),
                messageType = MessageType.Error()
            ),
            stateMessageCallback = object : StateMessageCallback {
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                GALLERY_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        activity?.let {
                            launchImageCrop(uri)
                        }
                    } ?: showImageSelectionError()
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE")
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE: uri: ${resultUri}")
                    viewModel.setUpdatedUri(resultUri)
                }

                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                    Log.d(TAG, "CROP: ERROR")
                    showImageSelectionError()
                }
            }
        }
    }

    fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            viewState.updatedBlogFields.let { updatedBlogFields ->
                setBlogProperties(
                    updatedBlogFields.updatedBlogTitle,
                    updatedBlogFields.updatedBlogBody,
                    updatedBlogFields.updatedImageUri
                )
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, { stateMessage ->

            stateMessage?.let {

                if (stateMessage.response.message.equals(SUCCESS_BLOG_UPDATED)) {
                    viewModel.updateListItem()
                }

                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    fun setBlogProperties(title: String?, body: String?, image: Uri?) {
        image?.let {
            requestManager
                .load(it)
                .into(binding.blogImage)
        }
        binding.blogTitle.setText(title)
        binding.blogBody.setText(body)
    }

    private fun saveChanges() {
        var multipartBody: MultipartBody.Part? = null
        viewModel.getUpdatedBlogUri()?.let { imageUri ->
            imageUri.path?.let { filePath ->
                val imageFile = File(filePath)
                Log.d(TAG, "UpdateBlogFragment, imageFile: file: ${imageFile}")
                if (imageFile.exists()) {
                    val requestBody =
                        RequestBody.create(
                            MediaType.parse("image/*"),
                            imageFile
                        )
                    // name = field name in serializer
                    // filename = name of the image file
                    // requestBody = file with file type information
                    multipartBody = MultipartBody.Part.createFormData(
                        "image",
                        imageFile.name,
                        requestBody
                    )
                }
            }
        }
        viewModel.setStateEvent(
            BlogStateEvent.UpdateBlogPostEvent(
                binding.blogTitle.text.toString(),
                binding.blogBody.text.toString(),
                multipartBody
            )
        )
        uiCommunicationListener.hideSoftKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                saveChanges()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onPause() {
        super.onPause()
        viewModel.setUpdatedTitle(binding.blogTitle.text.toString())
        viewModel.setUpdatedBody(binding.blogBody.text.toString())
    }
}






