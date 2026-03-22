const API_BASE_URL = "http://localhost:8080/api";

/**
 * Common API request function handling JWT tokens automatically
 */
async function apiRequest(endpoint, options = {}) {
    const token = localStorage.getItem('jwtToken');
    
    // Default headers
    const headers = {
        ...options.headers
    };

    // Only set application/json if not sending FormData and Content-Type isn't already specified
    if (!(options.body instanceof FormData) && !headers['Content-Type']) {
        headers['Content-Type'] = 'application/json';
    }

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const config = {
        ...options,
        headers: {
            ...headers,
            ...options.headers
        }
    };

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        
        // Let callers handle status themselves or throw if not ok
        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                // Token invalid or expired
                logout();
                throw new Error("Session expired. Please log in again.");
            }
            const errorText = await response.text();
            throw new Error(errorText || `API Request Failed with status ${response.status}`);
        }
        
        // return json if possible, or text for empty responses
        const textResponse = await response.text();
        if(!textResponse) return null;
        
        try {
            return JSON.parse(textResponse);
        } catch (e) {
            return textResponse;
        }
    } catch (error) {
        console.error(`Error in apiRequest to ${endpoint}:`, error);
        throw error;
    }
}

/**
 * Toast Notification Helper
 */
function showToast(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.style.cssText = 'position:fixed;bottom:20px;right:20px;display:flex;flex-direction:column;gap:10px;z-index:9999;';
        document.body.appendChild(container);
    }
    
    const toast = document.createElement('div');
    const bgColors = {
        success: '#10B981',
        error: '#EF4444',
        info: '#3B82F6',
        warning: '#F59E0B'
    };
    const bgColor = bgColors[type] || bgColors.info;
    
    toast.style.cssText = `background:${bgColor};color:white;padding:12px 20px;border-radius:6px;box-shadow:0 4px 6px rgba(0,0,0,0.1);font-size:14px;font-weight:500;opacity:0;transform:translateY(20px);transition:all 0.3s ease;display:flex;align-items:center;gap:8px;`;
    
    // Check if lucide is available for icons
    let icon = '';
    if (typeof lucide !== 'undefined') {
        const iconName = type === 'success' ? 'check-circle' : type === 'error' ? 'alert-circle' : 'info';
        icon = `<i data-lucide="${iconName}" style="width:16px;height:16px;"></i>`;
    }
    
    toast.innerHTML = `${icon}<span>${message}</span>`;
    container.appendChild(toast);
    
    if (typeof lucide !== 'undefined') lucide.createIcons();
    
    // Animate in
    setTimeout(() => {
        toast.style.opacity = '1';
        toast.style.transform = 'translateY(0)';
    }, 10);
    
    // Remove after 3s
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(20px)';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

/**
 * Form Loading State Helper
 */
function toggleBtnLoading(btn, isLoading, originalText = '') {
    if (!btn) return originalText;
    if (isLoading) {
        const currentHtml = btn.innerHTML;
        btn.innerHTML = '<i data-lucide="loader" class="animate-spin" style="animation: spin 1s linear infinite; width:16px; height:16px;"></i> Processing...';
        btn.disabled = true;
        if (typeof lucide !== 'undefined') lucide.createIcons();
        return currentHtml; // Return original to restore later
    } else {
        btn.innerHTML = originalText;
        btn.disabled = false;
        if (typeof lucide !== 'undefined') lucide.createIcons();
        return originalText;
    }
}

/**
 * Require authentication for accessing a page
 * @param {string[]} allowedRoles Array of allowed roles (e.g. ['ADMIN', 'STAFF']). Empty means any logged-in user.
 */
function requireAuth(allowedRoles = []) {
    const token = localStorage.getItem('jwtToken');
    const userStr = localStorage.getItem('user');
    
    // Redirect if no token or user
    if (!token || !userStr) {
        if (window.location.pathname.includes('/admin/') || 
            window.location.pathname.includes('/staff/') || 
            window.location.pathname.includes('/employee/')) {
            window.location.href = '../index.html';
        }
        return;
    }

    try {
        const user = JSON.parse(userStr);
        // If roles specified, check if user's role allows access
        if (allowedRoles.length > 0 && !allowedRoles.includes(user.role)) {
            // Unauthorised role, push back to login
            logout();
        }
    } catch(e) {
        // Bad user string
        logout();
    }
}

/**
 * Handle user logout universally
 */
function logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('user');
    // Assuming we might be nested one level deep (e.g., admin/index.html)
    // If not, standard index.html is the target
    if (window.location.pathname.includes('/admin/') || 
        window.location.pathname.includes('/staff/') || 
        window.location.pathname.includes('/employee/')) {
        window.location.href = '../index.html';
    } else {
        window.location.href = 'index.html';
    }
}
