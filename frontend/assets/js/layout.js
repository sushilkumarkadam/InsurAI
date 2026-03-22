/* layout.js – InsurAI Enterprise Shell */

const NAV = {
  admin: [
    { name: 'Dashboard',       path: 'index.html',           icon: 'layout-dashboard' },
    { name: 'Policies',        path: 'policies.html',         icon: 'file-text' },
    { name: 'Claims',          path: 'claims.html',           icon: 'alert-circle',  badge: 5 },
    { name: 'Renewals',        path: 'renewals.html',         icon: 'calendar' },
    { name: 'Risk Analysis',   path: 'risk-analysis.html',    icon: 'trending-up' },
    { name: 'Fraud Detection', path: 'fraud-detection.html',  icon: 'shield-alert',  badge: 2 },
    { name: 'Reports',         path: 'reports.html',          icon: 'bar-chart-2' },
    { name: 'User Mgmt',       path: 'user-management.html',  icon: 'users' },
    { name: 'Notifications',   path: 'notifications.html',    icon: 'bell',          badge: 7 },
    { name: 'Settings',        path: 'settings.html',         icon: 'settings' },
  ],
  staff: [
    { name: 'Dashboard',       path: 'index.html',           icon: 'layout-dashboard' },
    { name: 'Process Claims',  path: 'claims.html',          icon: 'alert-circle',   badge: 3 },
    { name: 'Notifications',   path: 'notifications.html',   icon: 'bell',           badge: 2 },
    { name: 'Profile',         path: 'profile.html',         icon: 'user' },
  ],
  employee: [
    { name: 'Dashboard',       path: 'index.html',           icon: 'layout-dashboard' },
    { name: 'My Policies',     path: 'policies.html',        icon: 'file-text' },
    { name: 'Submit Claim',    path: 'submit-claim.html',    icon: 'send' },
    { name: 'Track Claims',    path: 'track-claims.html',    icon: 'search' },
    { name: 'Notifications',   path: 'notifications.html',   icon: 'bell',           badge: 1 },
    { name: 'Profile',         path: 'profile.html',         icon: 'user' },
  ],
};

const ROLE_INFO = {
  admin:    { label: 'Administrator', initials: 'AD' },
  staff:    { label: 'Claims Staff',  initials: 'ST' },
  employee: { label: 'Employee',      initials: 'JD' },
};

function buildSidebar(role, activePath) {
  const items = NAV[role] || [];
  const info  = ROLE_INFO[role] || {};
  const links = items.map(item => {
    const active = activePath.endsWith(item.path);
    const badge = item.badge ? `<span class="nav-badge">${item.badge}</span>` : '';
    return `<li><a href="${item.path}" class="nav-item${active ? ' active' : ''}"><i data-lucide="${item.icon}"></i><span>${item.name}</span>${badge}</a></li>`;
  }).join('');

  return `
    <aside class="sidebar" id="sidebar">
      <div class="sidebar-logo">
        <div class="sidebar-logo-icon"><i data-lucide="shield"></i></div>
        <div class="sidebar-logo-text"><h2>InsurAI</h2><p>Corporate Platform</p></div>
      </div>
      <nav class="sidebar-nav">
        <div class="sidebar-section-label">Main Menu</div>
        <ul>${links}</ul>
      </nav>
      <div class="sidebar-profile">
        <div class="sidebar-avatar" id="sidebarInitial">${info.initials || 'U'}</div>
        <div class="sidebar-user-info"><strong id="sidebarUsername">User</strong><span>${info.label || 'Role'}</span></div>
        <a href="#" onclick="if(typeof logout === 'function') { logout(); } else { localStorage.clear(); window.location.href='../index.html'; }; return false;" class="sidebar-logout" title="Logout"><i data-lucide="log-out"></i></a>
      </div>
    </aside>
    <div class="sidebar-backdrop hidden" id="sidebarBackdrop"></div>`;
}

function buildTopbar(role) {
  const info = ROLE_INFO[role] || {};
  return `
    <header class="topbar">
      <div class="topbar-left">
        <button class="icon-btn" id="mobileMenuBtn"><i data-lucide="menu"></i></button>
        <div class="search-box">
          <i data-lucide="search"></i>
          <input type="text" placeholder="Search policies, claims, users…">
        </div>
      </div>
      <div class="topbar-right">
        <button class="icon-btn"><i data-lucide="bell"></i><span class="badge">3</span></button>
        <button class="icon-btn"><i data-lucide="help-circle"></i></button>
        <div class="topbar-profile">
          <div class="topbar-avatar" id="topbarInitial">${info.initials || 'U'}</div>
          <div class="topbar-profile-info"><strong id="topbarUsername">User</strong><span>${info.label || 'Role'}</span></div>
          <i data-lucide="chevron-down" style="width:14px;height:14px;color:var(--gray-400)"></i>
        </div>
      </div>
    </header>`;
}

function initLayout(role, activePath) {
  const app = document.getElementById('app-layout');
  if (!app) return;
  const content = app.innerHTML;
  app.innerHTML = `
    <div class="app-shell">
      ${buildSidebar(role, activePath)}
      <div class="main-content">
        ${buildTopbar(role)}
        <div class="page-content">${content}</div>
      </div>
    </div>`;
  if (typeof lucide !== 'undefined') lucide.createIcons();
  const btn      = document.getElementById('mobileMenuBtn');
  const sidebar  = document.getElementById('sidebar');
  const backdrop = document.getElementById('sidebarBackdrop');
  btn?.addEventListener('click', () => { sidebar?.classList.add('open'); backdrop?.classList.remove('hidden'); });
  backdrop?.addEventListener('click', () => { sidebar?.classList.remove('open'); backdrop?.classList.add('hidden'); });
  
  try {
      const userStr = localStorage.getItem('user');
      if(userStr) {
          const u = JSON.parse(userStr);
          const name = u.username || 'User';
          const init = name.substring(0, 2).toUpperCase();
          const sName = document.getElementById('sidebarUsername');
          const sInit = document.getElementById('sidebarInitial');
          const tName = document.getElementById('topbarUsername');
          const tInit = document.getElementById('topbarInitial');
          if(sName) sName.textContent = name;
          if(sInit) sInit.textContent = init;
          if(tName) tName.textContent = name;
          if(tInit) tInit.textContent = init;
      }
  } catch(e) {}
}
