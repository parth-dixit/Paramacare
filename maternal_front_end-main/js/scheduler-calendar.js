(function ($) {
  let weekdays = [
    "sunday",
    "monday",
    "tuesday",
    "wednesday",
    "thursday",
    "friday",
    "saturday",
  ];

  let months = [
    "january",
    "febuary",
    "march",
    "april",
    "may",
    "jun",
    "july",
    "auguest",
    "september",
    "october",
    "november",
    "december",
  ];

  Date.prototype.addDays = function (days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
  };

  Date.prototype.getMonthName = function () {
    return months[this.getMonth()];
  };

  Date.prototype.getMonthNextName = function () {
    let index = this.getMonth() < months.length - 1 ? this.getMonth() + 1 : 0;
    return months[index];
  };

  Date.prototype.printDate = function () {
    var date = "" + this.getDate();
    var month = "" + (this.getMonth() + 1);
    var year = this.getFullYear();
    if (date.length < 2) {
      date = "0" + date;
    }
    if (month.length < 2) {
      month = "0" + month;
    }
    return year + "-" + month + "-" + date;
  };

  dayTimeString = (day, date, time) => {
    return `${day}|${date}|${timeFormat(time)}`;
  };

  timeFormat = (time) => {
    let [hour, minute] = time.split(":");
    hour = hour.length < 2 ? "0" + hour : hour;
    return `${hour}:${minute}`;
  };

  let defaults = {
    startDate: new Date(),
    timeTemplates: {
      monday: [],
      tuesday: [],
      wednesday: [],
      thursday: [],
      friday: [],
      saturday: [],
      sunday: [],
    },
    availability: {},
  };

  $.fn.practitionerCalendar = function (opts) {
    let instance = this;
    let settings = $.extend({}, defaults, opts);
    let selections = [];
    let scheduleDays = 365;
    let days = [];
    let pagesCount = Math.ceil(scheduleDays / 7);
    let currentPage = 0;
    let autoAppliedPages = [];

    var getDates = (pageIndex) => {
      let startIndex = Math.max(0, pageIndex) * 7;
      return days.slice(startIndex, startIndex + 7);
    };

    var hasPrev = () => currentPage > 0;
    var hasNext = () => currentPage < pagesCount - 1;

    var onSubmit = settings.onSubmit;
    var loadTimes = settings.loadTimes;
    var loadAvailabities = settings.loadAvailabities;

    for (let index = 0; index < scheduleDays; index++) {
      days.push(settings.startDate.addDays(index));
    }

    if ($.isFunction(loadTimes)) {
      settings.timeTemplates = loadTimes.call(this, ...arguments) ?? {};
    }

    if ($.isFunction(loadAvailabities)) {
      settings.availability = loadAvailabities.call(this, ...arguments) ?? {};
    }

    this.hasModified = false;

    var selectDay = function (day, date, time) {
      var tmp = dayTimeString(day, date, timeFormat(time));

      let idx = selections.indexOf(tmp);
      if (idx !== -1) {
        selections.splice(idx, 1);
      } else {
        selections.push(tmp);
      }
    };

    var updateAvailability = function () {
      $(settings.availability).each(function (_, input) {
        const [date, time] = input.split("|");
        let pieces = date.split("-");
        day = weekdays[new Date(pieces[0], pieces[1], pieces[2]).getDay()];
        let item = dayTimeString(day, date, timeFormat(time));
        if (selections.indexOf(item) === -1) {
          selectDay(day, date, timeFormat(time));
        }
      });
    };

    var hasAutoApplyToggle = function () {
      if (hasPrev()) {
        return true;
      } else {
        return false;
      }
    };

    var hasAutoAppliedPage = function (pageIndex) {
      return autoAppliedPages.indexOf(pageIndex) !== -1;
    };

    var isSelectedTime = (day, date, time) => {
      if (date instanceof Date) {
        date = date.printDate();
      }

      return (
        selections.findIndex((item) => {
          let [_, date1, time1] = item.split("|");
          return date1 === date && time1 === timeFormat(time);
        }) !== -1
      );
    };

    var applySlots = function (pageIndex) {
      let dates = getDates(pageIndex - 1);
      for (let index = 0; index < dates.length; index++) {
        const date = dates[index];
        const newDate = date.addDays(7);
        let pattern = RegExp(`^.+${date.printDate()}.+`);
        const prevSlots = selections.filter((item) => pattern.test(item)) ?? [];

        prevSlots.forEach((item) => {
          let time = item.split("|")[2];
          selectDay(weekdays[newDate.getDay()], newDate.printDate(), time);
        });
      }
    };

    this.getMonthTitle = function () {
      var ret = ``;
      let daySlots = getDates(currentPage);
      let startMonth = daySlots[0].getMonthName().substring(0, 3).toUpperCase();
      let endMonth = daySlots[daySlots.length - 1]
        .getMonthNextName()
        .substring(0, 3)
        .toUpperCase();
      let year = settings.startDate.getFullYear();
      let attr = !hasAutoApplyToggle() ? "disabled" : "";
      let isApplied = hasAutoAppliedPage(currentPage) ? "checked" : "";
      ret += `
      <div class="sch-toolbar">
        <div class="custom-control custom-switch" id="sch-auto-apply-toggle" page="${currentPage}">
          <input type="checkbox" class="custom-control-input" ${attr} ${isApplied} id="customSwitch1">
          <label class="custom-control-label" for="customSwitch1">Apply from previous week?</label>
        </div>
      </div>
      `;

      ret += `<div class="sch-calendar-head">`;
      ret += `<div class="sch-nav-btn sch-nav-prev-week" ${
        !hasPrev() ? "disabled" : ""
      } title="Previous 7 days">`;
      ret += `<i class="material-icons">chevron_left</i>Previous`;
      ret += `</div>`;
      ret += `<div class="sch-month-title-text">`;
      ret += `${startMonth} - ${endMonth} ${year}`;
      ret += `</div>`;

      ret += `<div class="sch-nav-btn sch-nav-next-week" ${
        !hasNext() ? "disabled" : ""
      } title="Next 7 days">`;
      ret += `Next<i class="material-icons">chevron_right</i>`;
      ret += `</div>`;
      ret += `</div>`;
      return ret;
    };

    this.getDatesHeader = function () {
      var tmp = ``;
      let daySlots = getDates(currentPage);
      for (i = 0; i < daySlots.length; i++) {
        var dateInstance = daySlots[i];
        let day = weekdays[dateInstance.getDay()];
        let date = dateInstance.getDate();
        tmp += `<div class="sch-date-header" id="sch-date-header-${i}">`;
        tmp += `<div class="sch-date-number">${date}</div>`;
        tmp += `<div class="sch-date-display">${day
          .substring(0, 3)
          .toLocaleUpperCase()}</div>`;
        tmp += `</div>`;
      }
      var ret = `<div id="sch-dates-container">` + tmp + `</div>`;
      return ret;
    };

    this.getTimeSlots = function () {
      var tmp = ``;
      let daySlots = getDates(currentPage);
      for (i = 0; i < daySlots.length; i++) {
        var date = daySlots[i];
        let day = weekdays[date.getDay()];
        tmp += `<div class="sch-day-time-container" id="sch-day-time-container-${i}">`;
        $.each(settings.timeTemplates[day], function (_, time) {
          time = timeFormat(time);
          let isSelected = "";
          if (isSelectedTime(day, date, time)) {
            isSelected = "selected";
          }
          tmp += `<div class='time-slot'>`;
          tmp += `<div class="sch-available-time ${isSelected}"data-time="${time}" data-date="${date.printDate()}" data-day="${day}" >${time}</div>`;
          tmp += `</div>`;
        });
        tmp += `<div style="clear:both;"></div>`;
        tmp += `</div>`;
      }
      var ret = `<div id="sch-available-time-container">` + tmp + `</div>`;
      return ret;
    };

    let renderFooterSection = function () {
      let selectedDateContainer = `
   
      <p class="sch-footer-title">Selected dates / times:</p>
      <ul id="sch-selected-date-container"></ul>

      `;

      let submissionContainer = `
      <button id="sch-submit-button" disabled>Submit</button>
      `;

      let footerContainer = `
      <div id="sch-footer-container">
      <div id="sch-footer-left">
      ${selectedDateContainer}
      </div>
      <div id="sch-footer-right">
      ${submissionContainer}
      </div>
      </div>
      `;
      $("#sch-week-container").after(footerContainer);
      renderSelectedDates();
      if (instance.hasModified) {
        $("#sch-submit-button").removeAttr("disabled");
      }
    };

    let renderSelectedDates = function () {
      let element = $("#sch-selected-date-container");
      if (element.length != 0) {
        element.empty();
        $.each(selections, function (_, date) {
          var [day, date, time] = date.split("|");
          element.append(`
              <li class="sch-selected-date">
                <div class="sch-selected-date-date">${date}</div> 
                <div class="sch-selected-date-time">
                  <i class="material-icons">access_time</i>
                  <span>${time}</span>
                </div>
              </li>
              `);
        });
      }
    };

    let render = function () {
      updateAvailability();
      let ret = `<div id="sch-week-container">`;
      ret += instance.getMonthTitle();
      ret += instance.getDatesHeader();
      ret += instance.getTimeSlots();
      ret = ret + `</div>`;
      instance.html(ret);
      renderFooterSection();
    };

    render();

    this.getJson = function () {
      let _json = [];

      $.each(selections, function (_, item) {
        var [_, date, time] = item.split("|");
        var time = timeFormat(time);
        let input = `${date}|${time}`;
        if (_json.indexOf(input) == -1) {
          _json.push(input);
        }
      });
      return _json;
    };

    instance.on("click", ".sch-available-time", function (e) {
      var day = $(this).data("day");
      var date = $(this).data("date");
      var time = $(this).data("time");
      selectDay(day, date, timeFormat(time));
      instance.hasModified = true;
      render();
    });

    instance.on("click", "#sch-auto-apply-toggle", function (e) {
      let idx = autoAppliedPages.indexOf(currentPage);
      if (idx === -1) {
        applySlots(currentPage);
        autoAppliedPages.push(currentPage);
      } else {
        applySlots(currentPage);
        autoAppliedPages.splice(idx, 1);
      }
      render();
    });

    instance.on("click", "#sch-submit-button", function (e) {
      if (instance.hasModified) {
        if ($.isFunction(onSubmit)) {
          onSubmit.call(this, instance.getJson());
          instance.hasModified = false;
          render();
        }
      }
    });

    instance.on("click", ".sch-nav-btn.sch-nav-next-week", function (e) {
      if (hasNext()) {
        currentPage++;
        render();
      }
    });

    instance.on("click", ".sch-nav-btn.sch-nav-prev-week", function (e) {
      if (hasPrev()) {
        currentPage--;
        render();
      }
    });
  };

  // patient schedule
  $.fn.patientBookingCalendar = function (opts) {
    let selection = null;
    let instance = this;
    let settings = $.extend({}, defaults, opts);
    let bookingDays = 15;
    let days = [];
    let pagesCount = Math.ceil(bookingDays / 7);
    let currentPage = 0;

    var getDates = (pageIndex) => {
      let startIndex = Math.max(0, pageIndex) * 7;
      return days.slice(startIndex, startIndex + 7);
    };

    var hasPrev = () => currentPage > 0;
    var hasNext = () => currentPage < pagesCount - 1;

    var onLoadSlots = settings.onLoadSlots;
    var onSubmit = settings.onSubmit;

    for (let index = 0; index < bookingDays; index++) {
      days.push(settings.startDate.addDays(index));
    }

    var selectDay = (day, date, time) => {
      let dateTime = dayTimeString(day, date, time);
      if (selection === dateTime) {
        selection = null;
      } else {
        selection = dateTime;
      }
    };

    this.hasModified = () => {
      return selection != null;
    };

    this.getMonthTitle = function () {
      var ret = ``;

      let startMonth = settings.startDate
        .getMonthName()
        .substring(0, 3)
        .toUpperCase();
      let endMonth = settings.startDate
        .getMonthNextName()
        .substring(0, 3)
        .toUpperCase();
      let year = settings.startDate.getFullYear();

      ret += `<div class="sch-calendar-head">`;
      ret += `<div class="sch-nav-btn sch-nav-prev-week" ${
        !hasPrev() ? "disabled" : ""
      } title="Previous 7 days">`;
      ret += `<i class="material-icons">chevron_left</i>Previous`;
      ret += `</div>`;
      ret += `<div class="sch-month-title-text">`;
      ret += `${startMonth} - ${endMonth} ${year}`;
      ret += `</div>`;
      ret += `<div class="sch-nav-btn sch-nav-next-week" ${
        !hasNext() ? "disabled" : ""
      } title="Next 7 days">`;
      ret += `Next<i class="material-icons">chevron_right</i>`;
      ret += `</div>`;
      ret += `</div>`;
      return ret;
    };

    this.getDatesHeader = function () {
      var tmp = ``;
      let daySlots = getDates(currentPage);
      for (i = 0; i < daySlots.length; i++) {
        var dateInstance = daySlots[i];
        let day = weekdays[dateInstance.getDay()];
        let date = dateInstance.getDate();
        tmp += `<div class="sch-date-header" id="sch-date-header-${i}">`;
        tmp += `<div class="sch-date-number">${date}</div>`;
        tmp += `<div class="sch-date-display">${day
          .substring(0, 3)
          .toLocaleUpperCase()}</div>`;
        tmp += `</div>`;
      }
      var ret = `<div id="sch-dates-container">` + tmp + `</div>`;
      return ret;
    };

    this.getTimeSlots = function () {
      var tmp = ``;
      let slots = {};

      if ($.isFunction(onLoadSlots)) {
        slots = onLoadSlots.call(this, ...arguments) ?? {};
      }

      let daySlots = getDates(currentPage);
      for (i = 0; i < daySlots.length; i++) {
        var date = daySlots[i];
        let day = weekdays[date.getDay()];
        let times = slots[date.printDate()];
        tmp += `<div class="sch-day-time-container" id="sch-day-time-container-${i}">`;
        if (times != null) {
          $.each(times, function (_, time) {
            time = timeFormat(time);
            let isSelected =
              selection == dayTimeString(day, date.printDate(), time)
                ? "selected"
                : "";

            tmp += `<div class='time-slot'>`;
            tmp += `<div class="sch-available-time ${isSelected}" data-time="${time}" data-date="${date.printDate()}" data-day="${day}">${time}</div>`;
            tmp += `</div>`;
          });
        } else {
          tmp += `<div class='time-slot-empty'>`;
          tmp += `No Slots`;
          tmp += `</div>`;
        }
        tmp += `<div style="clear:both;"></div>`;
        tmp += `</div>`;
      }
      var ret = `<div id="sch-available-time-container">` + tmp + `</div>`;
      return ret;
    };

    let renderFooterSection = function () {
      let selectedDateContainer = `
   
      <p class="sch-footer-title">Selected Date / Time:</p>
      <ul id="sch-selected-date-container"></ul>

      `;

      let submissionContainer = `
      <button id="sch-submit-button" disabled>Submit</button>
      `;

      let footerContainer = `
      <div id="sch-footer-container">
      <div id="sch-footer-left">
      ${selectedDateContainer}
      </div>
      <div id="sch-footer-right">
      ${submissionContainer}
      </div>
      </div>
      `;
      $("#sch-week-container").after(footerContainer);
      renderSelectedDate();
      if (instance.hasModified()) {
        $("#sch-submit-button").removeAttr("disabled");
      }
    };

    let renderSelectedDate = function () {
      let element = $("#sch-selected-date-container");
      if (element.length != 0) {
        if (selection != null) {
          let [_, date, time] = selection.split("|");
          element.empty();
          element.append(`
          <li class="sch-selected-date">
            <div class="sch-selected-date-date">${date}</div> 
            <div class="sch-selected-date-time">
              <i class="material-icons">access_time</i>
              <span>${time}</span>
            </div>
          </li>
          `);
        }
      }
    };

    let render = function () {
      let ret = `<div id="sch-week-container">`;
      ret += instance.getMonthTitle();
      ret += instance.getDatesHeader();
      ret += instance.getTimeSlots();
      ret = ret + `</div>`;
      instance.html(ret);
      renderFooterSection();
    };
    render();

    instance.on("click", ".sch-available-time", function (e) {
      var day = $(this).data("day");
      var date = $(this).data("date");
      var time = $(this).data("time");
      selectDay(day, date, timeFormat(time));
      render();
    });

    instance.on("click", "#sch-submit-button", function (e) {
      if (instance.hasModified) {
        if ($.isFunction(onSubmit)) {
          let [day, date, time] = selection.split("|");
          onSubmit.call(this, { date: date, time: time, day: day });
          selection = null;
          render();
        }
      }
    });

    instance.on("click", ".sch-nav-btn.sch-nav-next-week", function (e) {
      if (hasNext()) {
        currentPage++;
        render();
      }
    });

    instance.on("click", ".sch-nav-btn.sch-nav-prev-week", function (e) {
      if (hasPrev()) {
        currentPage--;
        render();
      }
    });
  };
})(jQuery);
