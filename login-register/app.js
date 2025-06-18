const authModal = document.querySelector('.wrapper'); // it's wrapper div
const loginLink = document.querySelector('.login-link');
const registerLink = document.querySelector('.register-link');
const loginBtnModal = document.querySelector('.btnLogin-popup');
const closeBtnModal = document.querySelector('.close-btn-modal');
const profileBox = document.querySelector('.profile-box');
const avatarCircle = document.querySelector('.avatar-circle');
const alertBox = document.querySelector('.alert-box');

if (registerLink) registerLink.addEventListener('click', () => authModal.classList.add('slide'));
if (loginLink) loginLink.addEventListener('click', () => authModal.classList.remove('slide'));

if (loginBtnModal) loginBtnModal.addEventListener('click', () => authModal.classList.add('show'));
if (closeBtnModal) closeBtnModal.addEventListener('click', () => authModal.classList.remove('show', 'slide'));

// Profile dropdown functionality
if (avatarCircle && profileBox) {
    avatarCircle.addEventListener('click', (e) => {
        e.stopPropagation(); // Prevent event bubbling
        profileBox.classList.toggle('show');
    });

    // Close dropdown when clicking outside
    document.addEventListener('click', (e) => {
        if (!profileBox.contains(e.target)) {
            profileBox.classList.remove('show');
        }
    });
}

if (alertBox) {
    setTimeout(() => alertBox.classList.add('show'), 50);

    setTimeout(() => {
        alertBox.classList.remove('show');
        setTimeout(() => alertBox.remove(), 1000);
    }, 3000);   
}