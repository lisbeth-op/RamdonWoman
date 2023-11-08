package com.example.randomwoman.data.repository

import com.example.randomwoman.data.remote.PersonaApi
import com.example.randomwoman.data.remote.dto.PersonaDto
import com.example.randomwoman.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PersonRepository @Inject constructor(private val api: PersonaApi) {
    fun getPerson(): Flow<Resource<List<PersonaDto>>> = flow {
        try{
            emit(Resource.Loading())

            val person = api.getPersons()

            emit(Resource.Success(person.results))
        }catch (e: HttpException){
            emit(Resource.Error(e.message ?: "Error HTTP"))

        }catch (e: IOException){
            emit(Resource.Error(e.message ?: "Verificar conexion"))
        }
    }
}