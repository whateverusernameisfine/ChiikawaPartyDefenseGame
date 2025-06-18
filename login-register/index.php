<?php

session_start();

$username = $_SESSION['username'] ?? null;
$alerts = $_SESSION['alerts'] ?? [];
$active_form = $_SESSION['active_form'] ?? $_GET['show'] ?? '';

session_unset();

if ($username !== null) $_SESSION['username'] = $username;

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chiikawa Party Tower Defense</title>
    <link rel="stylesheet" href="style.css">
</head>

<body>
    <header>
        <h2 class="logo">CHIIKAWA</h2>
        <nav class="navigation">
            <a href="index.php">Home</a>
            <a href="#">About</a>
            <a href="leaderboard.php">Leaderboard</a>
            <a href="contact.php">Contact</a>
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
            <button class="btnLogin-popup">Login</button>
            <?php endif; ?>
        </div>
    </header>

    <?php if (!empty($username)) : ?>
    <div class="download-section">
        <div class="download-wrapper">
            <h3>Download the game to play on your computer!</h3>
            <a href="download.php" class="download-btn">
                Download Game Zip
                <small>(Windows, ~15MB)</small>
            </a>
            <div class="download-info">
                <p><strong>Requirements:</strong> Java 8 or higher</p>
                <p><strong>How to play:</strong> Detailed instruction in README.txt</p>
                <p><strong>Your scores will automatically sync to the online leaderboard!</strong></p>
            </div>
        </div>
    </div>
    <?php endif; ?>

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

    <div class="wrapper <?= $active_form === 'register' ? 'show slide' : ($active_form === 'login' ? 'show' : ''); ?>">
        <button type="button" class="close-btn-modal"><ion-icon name="close-outline"></ion-icon></button>
        
        <div class="form-box login">
            <h2>Login to Play</h2>
            <form action="auth_process.php" method="POST">
                <div class="input-box">
                    <span class="icon"><ion-icon name="person-outline"></ion-icon></span>
                    <input type="text" name="username" required>
                    <label>Username</label>
                </div>
                <div class="input-box">
                    <span class="icon"><ion-icon name="lock-closed-outline"></ion-icon></span>
                    <input type="password" name="password" required>
                    <label>Password</label>
                </div>
                <div class="remember-forgot">
                    <label><input type="checkbox"> Remember me</label>
                </div>
                <button type="submit" name='login-btn' class="btn">Login</button>
                <div class="login-register">
                    <p>Don't have an account? <a href="#" class="register-link">Register</a></p>
                </div>
            </form>
        </div>

        <div class="form-box register">
            <h2>Register</h2>
            <form action="auth_process.php" method="POST">
                <div class="input-box">
                    <span class="icon"><ion-icon name="person-outline"></ion-icon></span>
                    <input type="text" name='username' required>
                    <label>Your Username</label>
                </div>
                <div class="input-box">
                    <span class="icon"><ion-icon name="lock-closed-outline"></ion-icon></span>
                    <input type="password" name='password' required>
                    <label>Your Password</label>
                </div>
                <button type="submit" name="register-btn" class="btn">Register</button>
                <div class="login-register">
                    <p>Already have an account? <a href="#" class="login-link">Login</a></p>
                </div>
            </form>
        </div>

    </div>

    


    <script src="app.js"></script>
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
</body>
</html>