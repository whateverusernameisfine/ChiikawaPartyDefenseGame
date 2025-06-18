<?php

$host = 'localhost';
$user = 'root';
$password = '';
$database = 'user_db';

$conn = new mysqli($host, $user, $password, $database);

if ($conn->connect_error) {
    die('Connection failed: '. $conn->connect_error);
}

try {
    $pdo = new PDO("mysql:host=$host;dbname=$database", $user, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    die('PDO Connection failed: ' . $e->getMessage());
}

?>