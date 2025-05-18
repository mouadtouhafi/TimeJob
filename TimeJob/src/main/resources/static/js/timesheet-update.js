document.addEventListener('DOMContentLoaded', function() {
	const fromDateInput = document.getElementById('from-entry-date');
	const toDateInput = document.getElementById('to-entry-date');
	const container = document.getElementById('inputs-container');

	const startDate = new Date(fromDateInput.value);
	const endDate = new Date(toDateInput.value);

	const dayNames = ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'];

	if (!isNaN(startDate) && !isNaN(endDate) && startDate <= endDate) {
		const dayCount = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24)) + 1;

		for (let i = 0; i < dayCount; i++) {
			const currentDate = new Date(startDate);
			currentDate.setDate(startDate.getDate() + i);
			const formattedDate = currentDate.toISOString().split('T')[0];
			const dayIndex = currentDate.getDay();
			const div = document.createElement('div');
			div.className = 'day-input';

			const input = document.createElement('input');
			input.type = 'number';
			input.min = 0;
			input.max = 24;
			input.step = 1;
			input.required = true;
			input.name = dayNames[dayIndex] + 'Hours';
			input.placeholder = `Hours for ${dayNames[dayIndex].charAt(0).toUpperCase() + dayNames[dayIndex].slice(1)} (${formattedDate})`;


			input.addEventListener('keydown', (e) => {
				if (['.', ','].includes(e.key)) e.preventDefault();
			});

			input.addEventListener('input', function() {
				if (this.value > 24) this.value = 24;
				if (this.value < 0) this.value = 0;
				updateTotalHours();
			});

			div.appendChild(input);
			container.appendChild(div);
		}
	}

	function updateTotalHours() {
		const inputs = document.querySelectorAll('#inputs-container input[type="number"]');
		let total = 0;
		inputs.forEach(input => {
			const val = parseFloat(input.value);
			if (!isNaN(val)) total += val;
		});
		document.getElementById('hours').value = total;
	}
});