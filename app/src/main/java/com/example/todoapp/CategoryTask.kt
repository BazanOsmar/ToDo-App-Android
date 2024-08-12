package com.example.todoapp


sealed class CategoryTask{
    object Personal : CategoryTask()
    object Universidad: CategoryTask()
    object Otros: CategoryTask()
}