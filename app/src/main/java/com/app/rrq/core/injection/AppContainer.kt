package com.app.rrq.core.injection

import com.app.rrq.model.repository.AuthRepository
import com.app.rrq.model.usecase.LoginUseCase
import com.app.rrq.model.usecase.RegisterUseCase
import com.app.rrq.model.repository.LaporanRepository
import com.app.rrq.model.usecase.GetLaporansUseCase
import com.app.rrq.model.usecase.CreateLaporanUseCase
import com.app.rrq.model.usecase.GetLaporanByIdUseCase
import com.app.rrq.model.usecase.GetLaporansByUserUseCase
import com.app.rrq.model.usecase.UpdateStatusLaporanUseCase
import com.app.rrq.model.repository.PenggunaRepository
import com.app.rrq.model.usecase.GetPenggunasUseCase
import com.app.rrq.model.usecase.UpdateStatusPenggunaUseCase

object AppContainer {
    val laporanRepository: LaporanRepository by lazy {
        LaporanRepository()
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository()
    }

    val penggunaRepository: PenggunaRepository by lazy {
        PenggunaRepository()
    }

    val getLaporansUseCase: GetLaporansUseCase by lazy {
        GetLaporansUseCase(laporanRepository)
    }

    val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository)
    }

    val registerUseCase: RegisterUseCase by lazy {
        RegisterUseCase(authRepository)
    }

    val createLaporanUseCase: CreateLaporanUseCase by lazy {
        CreateLaporanUseCase(laporanRepository)
    }

    val getLaporanByIdUseCase: GetLaporanByIdUseCase by lazy {
        GetLaporanByIdUseCase(laporanRepository)
    }

    val getLaporansByUserUseCase: GetLaporansByUserUseCase by lazy {
        GetLaporansByUserUseCase(laporanRepository)
    }

    val updateStatusLaporanUseCase: UpdateStatusLaporanUseCase by lazy {
        UpdateStatusLaporanUseCase(laporanRepository)
    }

    val getPenggunasUseCase: GetPenggunasUseCase by lazy {
        GetPenggunasUseCase(penggunaRepository)
    }

    val updateStatusPenggunaUseCase: UpdateStatusPenggunaUseCase by lazy {
        UpdateStatusPenggunaUseCase(penggunaRepository)
    }
}
