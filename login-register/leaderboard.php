<?php
session_start();
require_once 'config.php';

$username = $_SESSION['username'] ?? null;

// Fetch all users ordered by score (highest first)
$stmt = $conn->prepare("SELECT username, score FROM users ORDER BY score DESC, username ASC");
$stmt->execute();
$result = $stmt->get_result();
$users = $result->fetch_all(MYSQLI_ASSOC);
$stmt->close();

// Find current user's rank if logged in
$current_user_rank = null;
if ($username) {
    foreach ($users as $index => $user) {
        if ($user['username'] === $username) {
            $current_user_rank = $index + 1;
            break;
        }
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chiikawa Party Tower Defense - Leaderboard</title>
    <link rel="stylesheet" href="style.css">
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background: url('images/background_leaderboard.png') no-repeat;
            background-size: cover;
            background-position: center;
        }

        /* Leaderboard Styles */
        .leaderboard-container {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 80vh;
            padding: 20px;
        }

        .leaderboard-wrapper {
            position: absolute;
            top: 130px;
            width: 600px;
            max-height: 70vh;
            background: transparent;
            border: 2px solid rgba(255, 255, 255, .5);
            border-radius: 20px;
            backdrop-filter: blur(20px);
            box-shadow: 0 0 30px rgba(0, 0, 0, .5);
            padding: 40px;
            overflow-y: auto;
        }

        .leaderboard-wrapper h2 {
            font-size: 3em;
            color: #162938;
            text-align: center;
            margin-bottom: 30px;
        }

        .leaderboard-table {
            width: 100%;
            border-collapse: collapse;
        }

        .leaderboard-table th {
            background: rgba(22, 41, 56, .1);
            color: #162938;
            font-weight: 600;
            padding: 15px 10px;
            text-align: left;
            border-bottom: 2px solid #162938;
        }

        .leaderboard-table td {
            padding: 12px 10px;
            border-bottom: 1px solid rgba(22, 41, 56, .2);
            color: #162938;
        }

        .leaderboard-table tbody tr:hover {
            background: rgba(255, 255, 255, .1);
        }

        .current-user {
            background: rgba(22, 41, 56, .15) !important;
            font-weight: 600;
        }

        .rank-column {
            width: 60px;
            text-align: center;
            font-weight: 600;
        }

        .username-column {
            width: auto;
        }

        .score-column {
            width: 80px;
            text-align: right;
            font-weight: 600;
        }

        .rank-badge {
            display: inline-block;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            background: #162938;
            color: aliceblue;
            text-align: center;
            line-height: 30px;
            font-size: 0.9em;
            font-weight: 600;
        }

        .rank-gold {
            background: linear-gradient(45deg, #FFD700, #FFA500);
            color: #162938;
        }

        .rank-silver {
            background: linear-gradient(45deg, #C0C0C0, #A8A8A8);
            color: #162938;
        }

        .rank-bronze {
            background: linear-gradient(45deg, #CD7F32, #B87333);
            color: aliceblue;
        }

        .back-btn {
            position: absolute;
            top: 10px;
            left: 10px;
            background: #162938;
            color: aliceblue;
            border: none;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            cursor: pointer;
            font-size: 1.2em;
            display: flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
        }

        .back-btn:hover {
            background: rgba(22, 41, 56, .8);
        }

        .current-user-info {
            text-align: center;
            margin-bottom: 20px;
            padding: 10px;
            background: rgba(22, 41, 56, .1);
            border-radius: 10px;
            color: #162938;
        }

        .no-users {
            text-align: center;
            color: #162938;
            font-style: italic;
            padding: 40px;
        }

        /* Scrollbar styling */
        .leaderboard-wrapper::-webkit-scrollbar {
            width: 8px;
        }

        .leaderboard-wrapper::-webkit-scrollbar-track {
            background: rgba(255, 255, 255, .1);
            border-radius: 10px;
        }

        .leaderboard-wrapper::-webkit-scrollbar-thumb {
            background: rgba(22, 41, 56, .3);
            border-radius: 10px;
        }

        .leaderboard-wrapper::-webkit-scrollbar-thumb:hover {
            background: rgba(22, 41, 56, .5);
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
            <button class="btnLogin-popup" onclick="window.location.href='index.php'">Login</button>
            <?php endif; ?>
        </div>
    </header>

    <div class="leaderboard-container">
        <div class="leaderboard-wrapper">
            <a href="index.php" class="back-btn">
                <ion-icon name="arrow-back-outline"></ion-icon>
            </a>
            
            <h2>🏆 Leaderboard</h2>
            
            <?php if ($username && $current_user_rank): ?>
            <div class="current-user-info">
                <strong>Your Rank: #<?= $current_user_rank ?></strong> | 
                Score: <?= $users[$current_user_rank - 1]['score'] ?>
            </div>
            <?php elseif ($username): ?>
            <div class="current-user-info">
                Welcome back, <strong><?= htmlspecialchars($username) ?></strong>! Play to get on the leaderboard!
            </div>
            <?php endif; ?>

            <?php if (empty($users)): ?>
            <div class="no-users">
                No players yet! Be the first to play and set a score!
            </div>
            <?php else: ?>
            <table class="leaderboard-table">
                <thead>
                    <tr>
                        <th class="rank-column">Rank</th>
                        <th class="username-column">Player</th>
                        <th class="score-column">Score</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($users as $index => $user): ?>
                    <tr <?= ($username && $user['username'] === $username) ? 'class="current-user"' : '' ?>>
                        <td class="rank-column">
                            <?php 
                            $rank = $index + 1;
                            $badge_class = '';
                            if ($rank === 1) $badge_class = 'rank-gold';
                            elseif ($rank === 2) $badge_class = 'rank-silver';
                            elseif ($rank === 3) $badge_class = 'rank-bronze';
                            ?>
                            <span class="rank-badge <?= $badge_class ?>"><?= $rank ?></span>
                        </td>
                        <td class="username-column">
                            <?= htmlspecialchars($user['username']) ?>
                            <?php if ($username && $user['username'] === $username): ?>
                            <span style="color: #162938; font-weight: bold;"> (You)</span>
                            <?php endif; ?>
                        </td>
                        <td class="score-column"><?= number_format($user['score']) ?></td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
            <?php endif; ?>
        </div>
    </div>

    <script src="app.js"></script>
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
</body>
</html>