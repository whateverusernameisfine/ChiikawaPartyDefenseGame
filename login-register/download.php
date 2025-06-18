<?php
session_start();
require_once 'config.php';

// Check if user is logged in (using username since user_id is not set)
if (!isset($_SESSION['username']) || empty($_SESSION['username'])) {
    header('Location: login.php');
    exit;
}

$username = $_SESSION['username'];

// Get user_id from database using username
try {
    $stmt = $pdo->prepare("SELECT id FROM users WHERE username = ?");
    $stmt->execute([$username]);
    $user = $stmt->fetch();
    
    if (!$user) {
        session_destroy();
        header('Location: login.php');
        exit;
    }
    
    $user_id = $user['id'];
} catch (Exception $e) {
    die('Database error: ' . $e->getMessage());
}

// Put your zip file in the same directory as this PHP file
$download_file = 'ChiikawaPartyDefense-Game.zip';

try {
    // Log download using the retrieved user_id
    $stmt = $pdo->prepare("INSERT INTO download_logs (user_id, download_time, ip_address) VALUES (?, NOW(), ?)");
    $stmt->execute([$user_id, $_SERVER['REMOTE_ADDR']]);
} catch (Exception $e) {
    error_log("Download logging failed: " . $e->getMessage());
}

// Force download
if (file_exists($download_file)) {
    // Clear any output buffer
    if (ob_get_level()) {
        ob_end_clean();
    }
    
    header('Content-Type: application/zip');
    header('Content-Disposition: attachment; filename="ChiikawaPartyDefense-Game.zip"');
    header('Content-Length: ' . filesize($download_file));
    header('Cache-Control: no-cache, must-revalidate');
    header('Pragma: public');
    
    readfile($download_file);
    exit;
} else {
    die('Game file not available. Please make sure ChiikawaPartyDefense-Game.zip is uploaded to: ' . __DIR__);
}
?>