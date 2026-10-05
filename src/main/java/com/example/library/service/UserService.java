// package com.example.library.service;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.example.library.dto.UserDto;
// import com.example.library.entity.User;
// import com.example.library.repository.UserReposiory;

// import jakarta.persistence.EntityNotFoundException;
// import jakarta.transaction.Transactional;

// @Service
// public class UserService {

//     @Autowired
//     private PasswordEncoder passwordEncoder;
    
//     @Autowired
//     private UserReposiory userReposiory;

//     public List<User> getAllUser(){
//         return userReposiory.findAll();
//     }

//     public User getById(long id){
//         return userReposiory.findById(id).get();
//     }

//     public User addUser(UserDto userDto){
//         if (userReposiory.findByUsername(userDto.getUsername()).isPresent()) {
//             throw new RuntimeException("Username already exists");
            
//         }
//         User user = new User();
//         user.setUsername(userDto.getUsername());
//         user.setNama(userDto.getNama());
//         user.setTanggalLahir(userDto.getTanggalLahir());
//         user.setAddress(userDto.getAddress());
//         user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//         return userReposiory.save(user);
//     }

//     @Transactional
//     public User updateUser(long id, UserDto userDto){
//         User user = userReposiory.findById(id).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
//         user.setNama(userDto.getNama());
//         user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//         user.setTanggalLahir(userDto.getTanggalLahir());
//         user.setAddress(userDto.getAddress());
//         user = userReposiory.save(user);
//         return user ;
//     }

//     public String delteUser(long id){
//         userReposiory.deleteById(id);
//         return "Data berhasil di hapus";
//     }
// }
