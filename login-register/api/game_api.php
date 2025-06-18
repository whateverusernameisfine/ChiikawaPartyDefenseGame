<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

require_once '../config.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['error' => 'Method not allowed']);
    exit;
}

$input = json_decode(file_get_contents('php://input'), true);
$action = $input['action'] ?? '';

try {
    switch ($action) {
        case 'login':
            handleLogin($conn, $input);
            break;
        case 'submit_score':
            handleScoreSubmission($conn, $input);
            break;
        default:
            http_response_code(400);
            echo json_encode(['error' => 'Invalid action']);
    }
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Server error: ' . $e->getMessage()]);
}

function handleLogin($conn, $input) {
    if (!isset($input['username']) || !isset($input['password'])) {
        http_response_code(400);
        echo json_encode(['error' => 'Username and password required']);
        return;
    }
    
    $username = $input['username'];
    $password = $input['password'];
    
    $stmt = $conn->prepare("SELECT username, password FROM users WHERE username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows === 1) {
        $user = $result->fetch_assoc();
        if (password_verify($password, $user['password'])) {
            echo json_encode(['success' => true, 'message' => 'Login successful']);
        } else {
            http_response_code(401);
            echo json_encode(['error' => 'Invalid credentials']);
        }
    } else {
        http_response_code(401);
        echo json_encode(['error' => 'Invalid credentials']);
    }
    
    $stmt->close();
}

function handleScoreSubmission($conn, $input) {
    if (!isset($input['username']) || !isset($input['score'])) {
        http_response_code(400);
        echo json_encode(['error' => 'Username and score required']);
        return;
    }
    
    $username = $input['username'];
    $newScore = intval($input['score']);
    
    // Update score only if the new score is higher
    $stmt = $conn->prepare("UPDATE users SET score = GREATEST(score, ?) WHERE username = ?");
    $stmt->bind_param("is", $newScore, $username);
    
    if ($stmt->execute() && $stmt->affected_rows > 0) {
        echo json_encode(['success' => true, 'message' => 'Score updated successfully']);
    } else {
        http_response_code(404);
        echo json_encode(['error' => 'User not found or score not updated']);
    }
    
    $stmt->close();
}

$conn->close();
?>