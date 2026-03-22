function toggleModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.toggle('hidden');
    }
}

function switchModal(closeModalId, openModalId) {
    toggleModal(closeModalId);
    toggleModal(openModalId);
}

function handleLogin(event) {
    event.preventDefault();
    const roleSelect = document.getElementById('loginRole');
    const role = roleSelect.value;
    
    // In a real app, you would authenticate here.
    // In our static prototype, we simply redirect:
    window.location.href = `${role}/index.html`;
}

function handleSignup(event) {
    event.preventDefault();
    const roleSelect = document.getElementById('signupRole');
    let role = roleSelect.value;
    
    // In our static prototype, we simply redirect:
    window.location.href = `${role}/index.html`;
}

// Close modals when clicking outside
window.addEventListener('click', (event) => {
    const loginModal = document.getElementById('loginModal');
    const signupModal = document.getElementById('signupModal');
    
    if (event.target === loginModal) {
        toggleModal('loginModal');
    }
    if (event.target === signupModal) {
        toggleModal('signupModal');
    }
});
