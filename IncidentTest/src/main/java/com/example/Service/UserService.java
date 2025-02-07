package com.example.Service;



import com.example.entity.User;
import com.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PinCodeService pinCodeService;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // Check if email is already registered
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new RuntimeException("Email is already registered");
        }
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Check if phone number is already registered
        Optional<User> existingUserByPhone = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (existingUserByPhone.isPresent()) {
            throw new RuntimeException("Phone number is already registered");
        }

        // Auto-fill city and country based on pin code
        String[] location = pinCodeService.getCityAndCountry(user.getPinCode());
        user.setCity(location[0]);
        user.setCountry(location[1]);

        return userRepository.save(user);
    }

    public User authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        throw new RuntimeException("Invalid email or password");
    }


    private Map<String, String> otpStorage = new HashMap<>(); // Store OTP temporarily

    // Generate OTP and return it in response (for testing)
    public String requestPasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Email not registered");
        }

        String otp = generateOTP();
        otpStorage.put(email, otp);
        return "Your OTP for password reset is: " + otp;  // In real-world, send OTP via email/SMS
    }

    // Validate OTP and reset password
    public String resetPassword(String email, String otp, String newPassword) {
        if (!otpStorage.containsKey(email) || !otpStorage.get(email).equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Email not found");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        otpStorage.remove(email);

        return "Password reset successful!";
    }

    // Generate 6-digit OTP
    private String generateOTP() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    }




