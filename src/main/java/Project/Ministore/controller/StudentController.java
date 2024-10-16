package Project.Ministore.controller;


import Project.Ministore.entity.StudentEntity;
import Project.Ministore.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Controller
public class StudentController {
	public static final long MAX_SIZE = 50 * 1024 * 1024; // 50 MB

	@Autowired
	StudentRepository studentRepository;

	@RequestMapping("/student")
	public String showNewStudentForm(Model model) {
		return "index_student";
	}
	@PostMapping("/insertImage")
	public ModelAndView insertStudent(@RequestParam("name") String name,
								@RequestParam("age") int age,
								@RequestParam("photo") MultipartFile photo) {
		try {
			// Create new StudentEntity and set fields
			StudentEntity student = new StudentEntity();
			student.setName(name);
			student.setAge(age);

			// Convert MultipartFile to byte array and set it
			if (!photo.isEmpty()) {
				byte[] imageData = photo.getBytes();
				student.setPhoto(imageData);
			}

			// Save student to the database
			studentRepository.save(student);

			return new ModelAndView("redirect:/fetch");
		} catch (Exception e) {
			return new ModelAndView("student", "msg", "Error: " + e.getMessage());
		}
	}


	@GetMapping(value = "/fetch")
	public ModelAndView listStudent(ModelAndView model) throws IOException {
		List<StudentEntity> listStu = (List<StudentEntity>) studentRepository.findAll();
		model.addObject("listStu", listStu);
		model.setViewName("student");
		return model;
	}

	@GetMapping(value = "/getStudentPhoto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getStudentPhoto(@PathVariable("id") long id) {
		StudentEntity student = studentRepository.findById(id).orElse(null);
		if (student != null && student.getPhoto() != null) {
			return ResponseEntity.ok()
					.contentType(MediaType.IMAGE_JPEG)
					.body(student.getPhoto());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
