(function() {
	const table = document.getElementById('timesheetTable');
	const globalInput = document.getElementById('globalFilter');
	const colInputs = Array.from(table.querySelectorAll('thead tr.filter-row input'));

	function applyFilters() {
		const globalVal = globalInput.value.trim().toLowerCase();
		const colVals = [];
		for (let i = 0; i < colInputs.length; i++) {
			const text = colInputs[i].value.trim().toLowerCase();
			colVals.push(text);
		}

		table.querySelectorAll('tbody tr').forEach(row => {
			const cells = Array.from(row.cells);
			let show = true;
			if (globalVal && !cells[2].textContent.toLowerCase().includes(globalVal)) {
				show = false;
			}
			colVals.forEach((f, idx) => {
				if (show && f && !cells[idx].textContent.toLowerCase().includes(f)) {
					show = false;
				}
			});

			row.style.display = show ? '' : 'none';
		});
	}
	globalInput.addEventListener('input', applyFilters);
	colInputs.forEach(i => i.addEventListener('input', applyFilters));
})();