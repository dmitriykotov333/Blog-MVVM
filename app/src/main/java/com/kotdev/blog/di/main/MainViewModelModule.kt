package com.kotdev.blog.di.main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotdev.blog.app.ui.viewmodels.main.AccountViewModel
import com.kotdev.blog.app.ui.viewmodels.main.CreateBlogViewModel
import com.kotdev.blog.app.ui.viewmodels.main.MainViewModelFactory
import com.kotdev.blog.app.ui.viewmodels.main.blog_viewmodel.BlogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: MainViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @MainViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accoutViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(BlogViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: BlogViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(CreateBlogViewModel::class)
    abstract fun bindCreateBlogViewModel(createBlogViewModel: CreateBlogViewModel): ViewModel
}