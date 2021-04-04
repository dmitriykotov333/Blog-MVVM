package com.kotdev.blog.di

import com.kotdev.blog.di.auth.AuthComponent
import com.kotdev.blog.di.main.MainComponent
import dagger.Module

@Module(
    subcomponents = [
        AuthComponent::class,
        MainComponent::class
    ]
)
class SubComponentsModule