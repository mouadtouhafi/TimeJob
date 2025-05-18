function switchTab(button) {
	document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
	document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
	const tabId = button.getAttribute('data-tab');
	button.classList.add('active');
	document.getElementById(tabId).classList.add('active');
}

function switchTab(button) {
	document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
	document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
	const tabId = button.getAttribute('data-tab');
	button.classList.add('active');
	document.getElementById(tabId).classList.add('active');
	localStorage.setItem("activeTabId", tabId);
}

window.addEventListener('DOMContentLoaded', () => {
	const savedTabId = localStorage.getItem("activeTabId");
	if (savedTabId) {
		const button = document.querySelector(`.tab-button[data-tab="${savedTabId}"]`);
		const tab = document.getElementById(savedTabId);
		if (button && tab) {
			document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
			document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
			button.classList.add('active');
			tab.classList.add('active');
		}
	}
});
function sortTimesheetsByUsername() {
	document.querySelectorAll('.tab-content table tbody').forEach(tbody => {
		const rows = Array.from(tbody.querySelectorAll('tr'));
		rows.sort((a, b) => {
			const userA = a.cells[0].textContent.trim().toLowerCase();
			const userB = b.cells[0].textContent.trim().toLowerCase();
			return userA.localeCompare(userB);
		});
		while (tbody.firstChild) {
			tbody.removeChild(tbody.firstChild);
		}
		rows.forEach(row => tbody.appendChild(row));
	});
}

window.addEventListener('DOMContentLoaded', () => {
	sortTimesheetsByUsername();
});

(function() {
	const tables = document.querySelectorAll('.tab-content table');
	tables.forEach(table => {
		const colInputs = Array.from(table.querySelectorAll('thead tr.filter-row input'));
		function applyFilters() {
			const colVals = colInputs.map(input => input.value.trim().toLowerCase());

			table.querySelectorAll('tbody tr').forEach(row => {
				const cells = Array.from(row.cells);
				let show = true;

				colVals.forEach((f, idx) => {
					if (show && f && !cells[idx]?.textContent.toLowerCase().includes(f)) {
						show = false;
					}
				});
				row.style.display = show ? '' : 'none';
			});
		}
		colInputs.forEach(input => input.addEventListener('input', applyFilters));
	});
})();

function openDuplicatedDatesModal() {
	document.getElementById("duplicatedDatesModal").style.display = "block";
}

function closeDuplicatedDatesModal() {
	document.getElementById("duplicatedDatesModal").style.display = "none";
}

window.onclick = function(event) {
	const modal = document.getElementById("duplicatedDatesModal");
	if (event.target === modal) {
		modal.style.display = "none";
	}
}

function toggleAccordion(button) {
	const content = button.nextElementSibling;
	const allContents = document.querySelectorAll(".accordion-content");
	const allButtons = document.querySelectorAll(".accordion-toggle");

	allContents.forEach(c => {
		if (c !== content) c.style.display = "none";
	});

	allButtons.forEach(b => {
		if (b !== button) b.classList.remove("active");
	});

	if (content.style.display === "block") {
		content.style.display = "none";
		button.classList.remove("active");
	} else {
		content.style.display = "block";
		button.classList.add("active");
	}
}

