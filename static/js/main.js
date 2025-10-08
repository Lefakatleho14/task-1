// Main JavaScript for School Management System

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);

    // Form validation
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Confirm delete actions
    const deleteButtons = document.querySelectorAll('.btn-outline-danger');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            if (confirm('Are you sure you want to delete this item?')) {
                // Add delete functionality here
                console.log('Delete confirmed');
            }
        });
    });

    // Attendance form enhancements
    const attendanceForm = document.querySelector('form[action*="mark_attendance"]');
    if (attendanceForm) {
        // Select all/none functionality
        const selectAllBtn = document.createElement('button');
        selectAllBtn.type = 'button';
        selectAllBtn.className = 'btn btn-sm btn-outline-primary me-2';
        selectAllBtn.innerHTML = '<i class="fas fa-check-square me-1"></i>Select All';
        
        const selectNoneBtn = document.createElement('button');
        selectNoneBtn.type = 'button';
        selectNoneBtn.className = 'btn btn-sm btn-outline-secondary';
        selectNoneBtn.innerHTML = '<i class="fas fa-square me-1"></i>Select None';
        
        const formActions = attendanceForm.querySelector('.d-grid');
        if (formActions) {
            formActions.insertBefore(selectAllBtn, formActions.firstChild);
            formActions.insertBefore(selectNoneBtn, selectAllBtn.nextSibling);
        }

        selectAllBtn.addEventListener('click', function() {
            const checkboxes = attendanceForm.querySelectorAll('input[name="attendance"]');
            checkboxes.forEach(checkbox => checkbox.checked = true);
        });

        selectNoneBtn.addEventListener('click', function() {
            const checkboxes = attendanceForm.querySelectorAll('input[name="attendance"]');
            checkboxes.forEach(checkbox => checkbox.checked = false);
        });
    }

    // Search functionality for tables
    const searchInputs = document.querySelectorAll('.table-search');
    searchInputs.forEach(input => {
        input.addEventListener('keyup', function() {
            const filter = this.value.toLowerCase();
            const table = this.closest('.card').querySelector('table');
            const rows = table.querySelectorAll('tbody tr');
            
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
    });

    // Auto-save form data to localStorage
    const formsToSave = document.querySelectorAll('form[method="POST"]');
    formsToSave.forEach(form => {
        const formId = form.action.split('/').pop() || 'form';
        
        // Load saved data
        const savedData = localStorage.getItem(`form_${formId}`);
        if (savedData) {
            const data = JSON.parse(savedData);
            Object.keys(data).forEach(key => {
                const input = form.querySelector(`[name="${key}"]`);
                if (input && input.type !== 'password') {
                    input.value = data[key];
                }
            });
        }
        
        // Save data on input change
        form.addEventListener('input', function(e) {
            if (e.target.name) {
                const formData = new FormData(form);
                const data = {};
                for (let [key, value] of formData.entries()) {
                    if (key !== 'password') {
                        data[key] = value;
                    }
                }
                localStorage.setItem(`form_${formId}`, JSON.stringify(data));
            }
        });
        
        // Clear saved data on successful submit
        form.addEventListener('submit', function() {
            localStorage.removeItem(`form_${formId}`);
        });
    });

    // Dashboard statistics animation
    const statNumbers = document.querySelectorAll('.h5.mb-0.font-weight-bold');
    statNumbers.forEach(stat => {
        const target = parseInt(stat.textContent);
        if (!isNaN(target)) {
            animateNumber(stat, 0, target, 1000);
        }
    });
});

// Utility function to animate numbers
function animateNumber(element, start, end, duration) {
    const startTime = performance.now();
    
    function updateNumber(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        const current = Math.floor(start + (end - start) * progress);
        
        element.textContent = current;
        
        if (progress < 1) {
            requestAnimationFrame(updateNumber);
        }
    }
    
    requestAnimationFrame(updateNumber);
}

// Utility function to show loading state
function showLoading(element) {
    element.classList.add('loading');
    const originalText = element.innerHTML;
    element.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Loading...';
    return originalText;
}

// Utility function to hide loading state
function hideLoading(element, originalText) {
    element.classList.remove('loading');
    element.innerHTML = originalText;
}

// Export functions for global use
window.SchoolManagement = {
    showLoading,
    hideLoading,
    animateNumber
};