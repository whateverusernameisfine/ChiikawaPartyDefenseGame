<?php
session_start();
require_once 'config.php';

if (isset($_POST['register-btn'])) {
    $username = $_POST['username'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);

    // Check if username exists using prepared statement
    $stmt = $conn->prepare("SELECT username FROM users WHERE username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows > 0) {
        $_SESSION['alerts'][] = [
            'type' => 'error',
            'message' => 'Username is already taken!'
        ];
        $_SESSION['active_form'] = 'register';
    } else {
        // Insert new user using prepared statement
        $stmt = $conn->prepare("INSERT INTO users (username, password) VALUES (?, ?)");
        $stmt->bind_param("ss", $username, $password);
        
        if ($stmt->execute()) {
            $_SESSION['alerts'][] = [
                'type' => 'success',
                'message' => 'Registration successful!'
            ];
            $_SESSION['active_form'] = 'login';
        } else {
            $_SESSION['alerts'][] = [
                'type' => 'error',
                'message' => 'Registration failed!'
            ];
            $_SESSION['active_form'] = 'register';
        }
    }
    $stmt->close();
    header('Location: index.php');
    exit();
}

if (isset($_POST['login-btn'])) {
    $username = $_POST['username'];
    $password = $_POST['password'];

    $stmt = $conn->prepare("SELECT * FROM users WHERE username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();
    $user = $result->fetch_assoc();

    if ($user && password_verify($password, $user['password'])) {
        $_SESSION['username'] = $user['username'];
        $_SESSION['alerts'][] = [
            'type' => 'success',
            'message' => 'Login successful!'
        ];
    } else {
        $_SESSION['alerts'][] = [
            'type' => 'error',
            'message' => 'Incorrect username or password!'
        ];
        $_SESSION['active_form'] = 'login';
    }
    
    $stmt->close();
    header('Location: index.php');
    exit();
}
?>