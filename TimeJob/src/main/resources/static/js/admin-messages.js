(function () {
	const table = document.getElementById('messagesTable');
	if (!table) return; // safety check

	const colInputs = Array.from(table.querySelectorAll('thead tr.filter-row input'));

	function applyFilters() {
		const colVals = colInputs.map(input => input.value.trim().toLowerCase());

		table.querySelectorAll('tbody tr').forEach(row => {
			const cells = Array.from(row.cells);
			let show = true;

			// Only apply filtering to columns that have inputs (first 3 columns)
			for (let i = 0; i < colVals.length; i++) {
				const filter = colVals[i];
				if (filter && !cells[i]?.textContent.toLowerCase().includes(filter)) {
					show = false;
					break;
				}
			}

			row.style.display = show ? '' : 'none';
		});
	}

	colInputs.forEach(input => input.addEventListener('input', applyFilters));
})();
