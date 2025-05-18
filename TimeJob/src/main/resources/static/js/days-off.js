const currentMonth = document.querySelector(".current-month");
let calendarDays = document.querySelector(".calendar-days");
let today = new Date();
let date = new Date();
let selectedDays = [];

today.setHours(0, 0, 0, 0);
currentMonth.textContent = date.toLocaleDateString("en-US", { month: 'long', year: 'numeric' });
renderCalendar();

function renderCalendar() {
	const prevLastDay = new Date(date.getFullYear(), date.getMonth(), 0).getDate();
	const totalMonthDay = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();
	const startWeekDay = new Date(date.getFullYear(), date.getMonth(), 1).getDay();
	calendarDays.innerHTML = "";
	const todayTime = today.getTime();

	for (let i = 0; i < 6 * 7; i++) {
		let day = i - startWeekDay + 1;
		let html = "";

		if (i < startWeekDay) {
			html = `<div class='padding-day'>${prevLastDay - startWeekDay + i + 1}</div>`;
		} else if (day <= totalMonthDay) {
			const thisDate = new Date(date.getFullYear(), date.getMonth(), day);
			thisDate.setHours(0, 0, 0, 0);
			const isToday = thisDate.getTime() === todayTime;
			const isSelected = selectedDays.some(d => d.getTime() === thisDate.getTime());

			html = `<div class='month-day${isToday ? ' current-day' : ''}${isSelected ? ' selected-day' : ''}' data-day='${day}'>${day}</div>`;
		} else {
			html = `<div class='padding-day'>${day - totalMonthDay}</div>`;
		}

		calendarDays.innerHTML += html;
	}

	document.querySelectorAll(".month-day").forEach(dayEl => {
		dayEl.addEventListener("click", () => {
			const day = parseInt(dayEl.getAttribute('data-day'));
			const selectedDate = new Date(date.getFullYear(), date.getMonth(), day);
			selectedDate.setHours(0, 0, 0, 0);
			const index = selectedDays.findIndex(d => d.getTime() === selectedDate.getTime());

			if (index > -1) {
				selectedDays.splice(index, 1);
				dayEl.classList.remove("selected-day");
			} else {
				if (selectedDays.length >= 7) {
					alert("You can only select up to 7 days off.");
					return;
				}
				selectedDays.push(selectedDate);
				dayEl.classList.add("selected-day");
			}

			updateSelectedDaysList();
		});
	});
}


function updateSelectedDaysList() {
	const listContainer = document.querySelector(".selected-days-list");
	listContainer.innerHTML = "";
	const sortedDates = selectedDays.slice().sort((a, b) => a - b);
	sortedDates.forEach(date => {
		const dayLabel = date.toLocaleDateString("en-US", { weekday: 'long', day: 'numeric', month: 'short', year: 'numeric' });
		const listItem = document.createElement("li");
		listItem.textContent = dayLabel;
		listContainer.appendChild(listItem);
	});
}
function prepareSubmission() {
	const input = document.getElementById("selectedDatesInput");
	const formatted = selectedDays.map(d =>
		d.getFullYear() + '-' +
		String(d.getMonth() + 1).padStart(2, '0') + '-' +
		String(d.getDate()).padStart(2, '0')
	);
	input.value = JSON.stringify(formatted);
}

document.querySelector(".prev").addEventListener("click", () => {
	date.setMonth(date.getMonth() - 1);
	currentMonth.textContent = date.toLocaleDateString("en-US", { month: 'long', year: 'numeric' });
	renderCalendar();
});

document.querySelector(".next").addEventListener("click", () => {
	date.setMonth(date.getMonth() + 1);
	currentMonth.textContent = date.toLocaleDateString("en-US", { month: 'long', year: 'numeric' });
	renderCalendar();
});

document.querySelector(".today").addEventListener("click", () => {
	date = new Date();
	currentMonth.textContent = date.toLocaleDateString("en-US", { month: 'long', year: 'numeric' });
	renderCalendar();
});

document.querySelector(".prev-year").addEventListener("click", () => {
	date.setFullYear(date.getFullYear() - 1);
	currentMonth.textContent = date.toLocaleDateString("en-US", { month: 'long', year: 'numeric' });
	renderCalendar();
});

document.querySelector(".next-year").addEventListener("click", () => {
	date.setFullYear(date.getFullYear() + 1);
	currentMonth.textContent = date.toLocaleDateString("en-US", { month: 'long', year: 'numeric' });
	renderCalendar();
});