package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.repository.AppRepository
import javax.inject.Inject

class ToggleWishlistUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke(id:String){
        return repository.toggleWishlist(id)
    }
}