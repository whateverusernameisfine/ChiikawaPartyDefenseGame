<?php

session_start();

$username = $_SESSION['username'] ?? null;
$alerts = $_SESSION['alerts'] ?? [];

// Clear alerts after displaying
session_unset();
if ($username !== null) $_SESSION['username'] = $username;

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact - Chiikawa Party Tower Defense</title>
    <link rel="stylesheet" href="style.css">
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background: url('images/background_contact.png') no-repeat;
            background-size: cover;
            background-position: center;
        }
    </style>
</head>

<body>
    <header>
        <h2 class="logo">CHIIKAWA</h2>
        <nav class="navigation">
            <a href="index.php">Home</a>
            <a href="#">About</a>
            <a href="leaderboard.php">Leaderboard</a>
            <a href="contact.php" class="active">Contact</a>
        </nav>

        <div class="user-auth">
            <?php if (!empty($username)) : ?>
            <div class="profile-box">
                <div class="avatar-circle"><?= strtoupper($username[0]); ?></div>
                <div class="dropdown">
                    <a href="#">My Account</a>
                    <a href="logout.php">Logout</a>
                </div>
            </div>
            <?php else: ?>
            <button class="btnLogin-popup" onclick="window.location.href='index.php?show=login'">Login</button>
            <?php endif; ?>
        </div>
    </header>

    <?php if (!empty($alerts)) : ?>
    <div class="alert-box">
        <?php foreach ($alerts as $alert): ?>
        <div class="alert <?= $alert['type']; ?>">
            <ion-icon name="<?= $alert['type'] === 'success' ? "checkbox" : "close"; ?>"></ion-icon>
            <span><?= $alert['message']; ?></span>
        </div>
        <?php endforeach; ?>
    </div>
    <?php endif; ?>

    <main class="contact-main">
        <div class="contact-box">
            <h1>Contact Us</h1>
            
            <div class="contact-info">
                <p>Have questions about Chiikawa Party Tower Defense? Need help with the game or found a bug? We'd love to hear from you!</p>
                
                <div class="contact-details">
                    <div class="contact-item">
                        <ion-icon name="mail-outline"></ion-icon>
                        <span>24binh.dth@vinuni.edu.vn</span>
                    </div>
                    <div class="contact-item">
                        <ion-icon name="time-outline"></ion-icon>
                        <span>9am - 5pm</span>
                    </div>
                    <div class="contact-item">
                        <ion-icon name="location-outline"></ion-icon>
                        <span>VinUniversity</span>
                    </div>
                </div>

                <div class="additional-info">
                    <h3>Support</h3>
                    <p>For technical issues, account problems, or general inquiries, please reach out to us via email.</p>
                </div>
            </div>
        </div>
    </main>

    <script src="app.js"></script>
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
</body>
</html>