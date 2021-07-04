package com.example.freelancingapplication

import Model.Application
import Model.Job
import Model.User
import api.ServiceBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import repository.AppRepository
import repository.JobRepository
import repository.UserRepository

class APITest {

    @Test
    fun registerUser() = runBlocking {
        val user =
                User(Fullname = "John Shrestha", Email = "john@gmail.com", Usertype = "Job Owner",Username = "john11", Password = "555555")
        val userRepository = UserRepository()
        val response = userRepository.registerUser(user)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkLoginOwner() = runBlocking {
        val userRepository = UserRepository()
        val response = userRepository.checkUser("abistha01", "111111")
        ServiceBuilder.token = response.token
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun addJob() = runBlocking {
        ServiceBuilder.currentuser = UserRepository().checkUser("abijeet","333333").data
        ServiceBuilder.token="Bearer "+UserRepository().checkUser("abijeet","333333").token
        val jobs =
                Job(JobTitle = "3rd assignment",JobOwner = ServiceBuilder.currentuser?.Fullname,Description = "Need to finish android assignment",
                SkillsNeeded = "Android Development",BudgetTime = "40000 and 40 hrs", Userid = ServiceBuilder.currentuser?._id)
        val job = JobRepository()

        val response = job.postJob(jobs)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }
    @Test
    fun updateJob() = runBlocking {
        ServiceBuilder.currentuser = UserRepository().checkUser("abijeet","333333").data
        ServiceBuilder.token="Bearer "+UserRepository().checkUser("abijeet","333333").token
        val jobs =
                Job(JobTitle = "third Year assignment",JobOwner = ServiceBuilder.currentuser?.Fullname,Description = "Need to finish android assignment",
                SkillsNeeded = "Android Development",BudgetTime = "50000 and 40 hrs", Userid = ServiceBuilder.currentuser?._id)
        val job = JobRepository()

        val response = job.updateone(id = "60779926baf8822c306f1023",job = jobs)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun deletejob() = runBlocking {
            ServiceBuilder.token="Bearer "+UserRepository().checkUser("abijeet","333333").token

        val jobs = JobRepository()
        val response = jobs.deleteone("60779926baf8822c306f1023")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }


    @Test
    fun checkLoginApplicant() = runBlocking {
        val userRepository = UserRepository()
        val response = userRepository.checkUser("abinin", "222222")
        ServiceBuilder.token = response.token
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun addApplication() = runBlocking {
        ServiceBuilder.token="Bearer "+UserRepository().checkUser("abinin","222222").token
        ServiceBuilder.currentuser = UserRepository().checkUser("abinin","222222").data

        val app =
                Application(JobName = "3rd assignment",JobOwner = "Prajwal Shrestha",
                        UserName = ServiceBuilder.currentuser?.Fullname,
                        Email = ServiceBuilder.currentuser?.Email)
        val appss = AppRepository()
        val response = appss.createApplication(app)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }
    @Test
    fun updateApplication() = runBlocking {
        val appss = AppRepository()
        ServiceBuilder.token="Bearer "+UserRepository().checkUser("abijeet","333333").token
        val response = appss.updateApp("6077998cbaf8822c306f1024",Accepted = "true")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }
    @Test
    fun getApplication() = runBlocking {
        ServiceBuilder.token="Bearer "+UserRepository().checkUser("abinin","222222").token
        ServiceBuilder.currentuser = UserRepository().checkUser("abinin","222222").data

        val appss = AppRepository()
        val response = appss.getApplicationA(ServiceBuilder.currentuser?.Fullname!!)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun cancelApplication() = runBlocking {
        ServiceBuilder.token="Bearer "+UserRepository().checkUser("abinin","222222").token
        val appss = AppRepository()
        val response = appss.deleteApp("6077998cbaf8822c306f1024")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }



}