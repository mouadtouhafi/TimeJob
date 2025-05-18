(function() {
	const table = document.getElementById('timesheetTable');
	const colInputs = Array.from(table.querySelectorAll('thead tr.filter-row input'));

	function applyFilters() {
		const colVals = colInputs.map(input => input.value.trim().toLowerCase());

		table.querySelectorAll('tbody tr').forEach(row => {
			const cells = Array.from(row.cells);
			let show = true;

			colVals.forEach((filter, idx) => {
				if (filter && !cells[idx]?.textContent.toLowerCase().includes(filter)) {
					show = false;
				}
			});

			row.style.display = show ? '' : 'none';
		});
	}

	colInputs.forEach(input => input.addEventListener('input', applyFilters));
})();
