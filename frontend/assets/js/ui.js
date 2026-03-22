// UI Components Logic

document.addEventListener('DOMContentLoaded', () => {
  initTabs();
  initModals();
  initAccordions();
});

// --- Tabs ---
function initTabs() {
  const tabTriggers = document.querySelectorAll('[data-tab-group]');
  
  tabTriggers.forEach(trigger => {
    trigger.addEventListener('click', (e) => {
      const group = trigger.getAttribute('data-tab-group');
      const targetId = trigger.getAttribute('data-tab-target');
      
      // Deactivate all triggers in this group
      document.querySelectorAll(`[data-tab-group="${group}"]`).forEach(t => {
        t.setAttribute('aria-selected', 'false');
        t.classList.remove('bg-white', 'text-gray-900', 'shadow-sm');
        t.classList.add('text-gray-500', 'hover:text-gray-700');
      });
      
      // Activate clicked trigger
      trigger.setAttribute('aria-selected', 'true');
      trigger.classList.remove('text-gray-500', 'hover:text-gray-700');
      trigger.classList.add('bg-white', 'text-gray-900', 'shadow-sm');
      
      // Hide all panels in this group
      document.querySelectorAll(`[data-tab-panel="${group}"]`).forEach(panel => {
        panel.classList.add('hidden');
      });
      
      // Show target panel
      const targetPanel = document.getElementById(targetId);
      if(targetPanel) {
        targetPanel.classList.remove('hidden');
      }
    });
  });
}

// --- Modals ---
function initModals() {
  const modalTriggers = document.querySelectorAll('[data-modal-target]');
  const modalCloses = document.querySelectorAll('[data-modal-close]');
  
  modalTriggers.forEach(trigger => {
    trigger.addEventListener('click', () => {
      const targetId = trigger.getAttribute('data-modal-target');
      const modal = document.getElementById(targetId);
      if(modal) {
        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden'; // Prevent background scrolling
      }
    });
  });
  
  modalCloses.forEach(close => {
    close.addEventListener('click', () => {
      const targetId = close.getAttribute('data-modal-close');
      const modal = document.getElementById(targetId);
      if(modal) {
        modal.classList.add('hidden');
        document.body.style.overflow = '';
      }
    });
  });
  
  // Close on backdrop click
  document.querySelectorAll('.modal-backdrop').forEach(backdrop => {
    backdrop.addEventListener('click', (e) => {
      if(e.target === backdrop) {
        backdrop.classList.add('hidden');
        document.body.style.overflow = '';
      }
    });
  });
}

// --- Accordions ---
function initAccordions() {
  const accordionHeaders = document.querySelectorAll('[data-accordion-header]');
  
  accordionHeaders.forEach(header => {
    header.addEventListener('click', () => {
      const targetId = header.getAttribute('data-accordion-target');
      const content = document.getElementById(targetId);
      const icon = header.querySelector('[data-accordion-icon]');
      
      const isExpanded = header.getAttribute('aria-expanded') === 'true';
      
      if(isExpanded) {
        header.setAttribute('aria-expanded', 'false');
        content.style.maxHeight = null;
        content.classList.add('hidden');
        if(icon) icon.classList.remove('rotate-180');
      } else {
        header.setAttribute('aria-expanded', 'true');
        content.classList.remove('hidden');
        content.style.maxHeight = content.scrollHeight + 'px';
        if(icon) icon.classList.add('rotate-180');
      }
    });
  });
}
