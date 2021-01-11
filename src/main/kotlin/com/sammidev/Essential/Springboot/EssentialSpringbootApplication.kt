package com.sammidev.Essential.Springboot

// © 2021 sammidev

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.Period
import java.util.*
import kotlin.jvm.Transient

@SpringBootApplication
class EssentialSpringbootApplication

fun main(args: Array<String>) {
	runApplication<EssentialSpringbootApplication>(*args)
}

data class Student (
		val id: String,
		var email: String,
		val dob: LocalDate,
		@Transient
	val age : Int = Period.between(dob, LocalDate.now()).years,
		val createdAt: LocalDate? = LocalDate.now()
)

@Service
class StudentService {

	private var studentList = mutableListOf<Student>(
			Student(id = UUID.randomUUID().toString(), email = "sammidev@gmail.com", dob = LocalDate.of(2001, 10, 20)),
			Student(id = UUID.randomUUID().toString(), email = "ayatullah@gmail.com", dob = LocalDate.of(1972, 10, 20)),
			Student(id = UUID.randomUUID().toString(), email = "aditya@gmail.com", dob = LocalDate.of(2002, 10, 20))
	)

	fun findAllStudent(): MutableList<Student> = this.studentList

	fun findStudentById(id: String): Student? {
		for (student in this.studentList) {
			if (id.equals(student.id, ignoreCase = true)) {
				return student
			}
		}
		return null
	}

	fun deleteStudent(student: Student): Boolean {
		for (student in this.studentList) {
			this.studentList.remove(student)
			return true
		}
		return false
	}

	fun deleteStudentById(id: String): Boolean {
		val student = findStudentById(id)
		if (student != null) {
			this.studentList.remove(student)
			return true;
		}
		return false
	}

	fun createStudent(student: Student): Boolean {
		require(student.id != null || student.email != null || student.dob != null)

		for (student in this.studentList) {
			if (student.email.equals(this.studentList.forEach { i -> i.email })) {
				throw IllegalStateException("email taken")
			}
		}

		this.studentList.add(student)
		return true
	}

	fun updateStudent(id: String, email: String): Boolean {
		val student = findStudentById(id)

		if (student != null) {
			this.studentList.forEach {
				if (it.email.equals(email)) {
				} else {
					it.email = email
					return true
				}
			}
		}
		throw IllegalStateException("id not found")
	}
}
@RestController
@RequestMapping("api/v1/students")
class StudentController {

	@Autowired lateinit var studentService: StudentService

	@GetMapping
	fun findAllStudent(): MutableIterable<Student> {
		return this.studentService.findAllStudent()
	}

	@GetMapping("/{id}")
	fun findStudentById(@PathVariable(name = "id") id: String): Student? {
		return this.studentService.findStudentById(id)
	}

	@PostMapping
	fun createStudent(@RequestBody student: Student): Boolean {
		return this.studentService.createStudent(student)
	}

	@DeleteMapping("/{id}")
	fun deleteStudentById(@PathVariable(name = "id") id: String): Boolean {
		return this.studentService.deleteStudentById(id);
	}

	@DeleteMapping
	fun deleteStudent(@RequestBody student: Student): Boolean {
		return this.studentService.deleteStudent(student)
	}

	@PutMapping("/update/{id}")
	fun updateStudent(@PathVariable(name = "id") id: String, @RequestBody email: String): Boolean {
		return this.studentService.updateStudent(id, email)
	}

	/**
	 * body for put endpoint :
	 *
	 * emailUpdate@gmail.com
	 *
	 * nb : not contains {, }, direct write email OKEEEH
	 */
}