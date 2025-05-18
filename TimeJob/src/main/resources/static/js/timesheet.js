document.addEventListener('DOMContentLoaded', function () {
    const startDateInput = document.getElementById('from-entry-date');
    const endDateInput = document.getElementById('to-entry-date');
    const dailyHoursContainer = document.getElementById('daily-hours-container');
    const totalHoursInput = document.getElementById('hours');

    const dayNames = ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'];

    endDateInput.disabled = true;

    startDateInput.addEventListener('change', function () {
        if (this.value) {
            endDateInput.value = '';
            endDateInput.disabled = false;
            const startDate = new Date(this.value);
            const minEndDate = new Date(startDate);
            const maxEndDate = new Date(startDate);
            maxEndDate.setDate(maxEndDate.getDate() + 6);

            const minDateString = minEndDate.toISOString().split('T')[0];
            const maxDateString = maxEndDate.toISOString().split('T')[0];

            endDateInput.min = minDateString;
            endDateInput.max = maxDateString;

            if (!endDateInput.value || new Date(endDateInput.value) > maxEndDate) {
                endDateInput.value = maxDateString;
            }
        } else {
            endDateInput.disabled = true;
            endDateInput.value = '';
        }
    });

    document.querySelector('form').addEventListener('submit', function (e) {
        const start = new Date(startDateInput.value);
        const end = new Date(endDateInput.value);
        const diffDays = Math.ceil((end - start) / (1000 * 60 * 60 * 24));

        if (diffDays > 6) {
            e.preventDefault();
            alert('Maximum date range allowed is 7 days');
        }
    });

    function createDailyHoursInputs(startDate, endDate) {
        dailyHoursContainer.innerHTML = '';

        const parent = dailyHoursContainer.parentNode;
        const existingTimeLabel = parent.querySelector('.time-label-group');
        if (existingTimeLabel) {
            parent.removeChild(existingTimeLabel);
        }

        const timeLabelDiv = document.createElement('div');
        timeLabelDiv.className = 'form-group-label time-label-group';
        timeLabelDiv.id = 'time-label';
        const timeLabel = document.createElement('label');
        timeLabel.textContent = 'Hours of work per day:';
        timeLabelDiv.appendChild(timeLabel);
        parent.insertBefore(timeLabelDiv, dailyHoursContainer);

        const diffTime = endDate - startDate;
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;

        for (let i = 0; i < diffDays; i++) {
            const currentDate = new Date(startDate);
            currentDate.setDate(startDate.getDate() + i);
            const dayIndex = currentDate.getDay();
            const dayName = dayNames[dayIndex];

            const div = document.createElement('div');
            div.className = 'day-input';

            const label = document.createElement('label');
            label.textContent = dayName.charAt(0).toUpperCase() + dayName.slice(1);

            const input = document.createElement('input');
            input.type = 'number';
            input.min = 0;
            input.max = 24;
            input.step = 1;
            input.required = true;
            input.name = dayName + 'Hours';
           // input.name = `dailyHours['${dateString}']`;
            input.placeholder = 'e.g., 4';

            input.addEventListener('keydown', (e) => {
                if (['.', ','].includes(e.key)) e.preventDefault();
            });

            input.addEventListener('input', updateTotalHours);

            div.appendChild(label);
            div.appendChild(input);
            dailyHoursContainer.appendChild(div);
        }
    }

    function updateTotalHours() {
        const inputs = dailyHoursContainer.querySelectorAll('input[type="number"]');
        let sum = 0;
        inputs.forEach(i => {
            const val = parseFloat(i.value) || 0;
            sum += val;
        });
        totalHoursInput.value = sum;
    }

    [startDateInput, endDateInput].forEach(input => {
        input.addEventListener('change', () => {
            if (startDateInput.value && endDateInput.value) {
                const startDate = new Date(startDateInput.value);
                const endDate = new Date(endDateInput.value);
                createDailyHoursInputs(startDate, endDate);
            } else {
                dailyHoursContainer.innerHTML = '';
            }
        });
    });
});
