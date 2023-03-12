(function ($) {
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
    colors: {
      bookedSlot: {
        text: "#585858",
        bg: "#8e8e",
      },
    },
  };

  // practitioner scheduling starts
  $.fn.practitionerCalendar = function (opts) {
    let instance = this;
    let settings = $.extend({}, defaults, opts);
    let selections = [];
    let bookedSlots = [];
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

    var loadTimes = settings.loadTimes;
    var onLoadAvailablities = settings.onLoadAvailablities;
    var onSubmit = settings.onSubmit;
    var onError = settings.onError;
    var onSuccess = settings.onSuccess;

    this.hasModified = false;

    var isBeforeSlot = (date, hour) => {
      if (date instanceof Date) {
        date = date.printDate();
      }
      if (!(hour instanceof Number)) {
        hour = Number.parseInt(hour);
      }
      let hourMatched = settings.startDate.getHours() >= hour;
      let dateMatched = settings.startDate.printDate() === date;
      return hourMatched && dateMatched;
    };

    var bookedDay = function (day, date, time) {
      var tmp = dayTimeString(day, date, time);
      let idx = bookedSlots.indexOf(tmp);

      if (idx !== -1) {
        bookedSlots.splice(idx, 1);
      } else {
        bookedSlots.push(tmp);
      }
    };

    var selectDay = function (day, date, time) {
      var tmp = dayTimeString(day, date, time);
      let idx = selections.indexOf(tmp);

      if (idx !== -1) {
        selections.splice(idx, 1);
      } else {
        selections.push(tmp);
      }
    };

    var updateAvailability = function (selectedDate) {
      $(selectedDate).each(function (_, input) {
        const [day, date, time] = input.split("|");
        let isBefore = isBeforeSlot(date, time);
        if (!isBefore) {
          let item = dayTimeString(day, date, time);
          if (selections.indexOf(item) === -1) {
            selectDay(day, date, time);
          }
        }
      });
    };

    var updateBooked = function (bookedDate) {
      $(bookedDate).each(function (_, input) {
        const [day, date, time] = input.split("|");
        let isBefore = isBeforeSlot(date, time);
        if (!isBefore) {
          let item = dayTimeString(day, date, time);
          if (bookedSlots.indexOf(item) === -1) {
            bookedDay(day, date, time);
          }
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
          return date1 === date && time1 === time;
        }) !== -1
      );
    };

    var isBookedTime = (day, date, time) => {
      if (date instanceof Date) {
        date = date.printDate();
      }

      return (
        bookedSlots.findIndex((item) => {
          let [_, date1, time1] = item.split("|");
          return date1 === date && time1 === time;
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
          selectDay(__schWeekdays[newDate.getDay()], newDate.printDate(), time);
        });
      }
    };

    this.getMonthTitle = function () {
      var ret = ``;
      let daySlots = getDates(currentPage);
      let startMonth = daySlots[0].getMonthName().substring(0, 3).toUpperCase();
      let endMonth = daySlots[daySlots.length - 1].getMonthNextName().substring(0, 3).toUpperCase();
      let year = settings.startDate.getFullYear();
      let attrs = [];
      if (!hasAutoApplyToggle()) {
        attrs.push("disabled");
      }
      if (hasAutoAppliedPage(currentPage)) {
        attrs.push("checked");
      }

      ret += `
        <div class="sch-toolbar">
          <div class="custom-control custom-switch" id="sch-auto-apply-toggle" page="${currentPage}">
            <input type="checkbox" class="custom-control-input" ${attrs.join(" ")} id="auto_applied_${currentPage}">
            <label class="custom-control-label" for="auto_applied_${currentPage}">Apply from previous week?</label>
          </div>
        </div>
        `;

      ret += `<div class="sch-calendar-head">`;
      ret += `<div class="sch-nav-btn sch-nav-prev-week" ${!hasPrev() ? "disabled" : ""} title="Previous 7 days">`;
      ret += `<i class="material-icons">chevron_left</i>Previous`;
      ret += `</div>`;
      ret += `<div class="sch-month-title-text">`;
      ret += `${startMonth} ${year}`;
      ret += `</div>`;

      ret += `<div class="sch-nav-btn sch-nav-next-week" ${!hasNext() ? "disabled" : ""} title="Next 7 days">`;
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
        let day = __schWeekdays[dateInstance.getDay()];
        let date = dateInstance.getDate();
        tmp += `<div class="sch-date-header" id="sch-date-header-${i}">`;
        tmp += `<div class="sch-date-number">${date}</div>`;
        tmp += `<div class="sch-date-display">${day.substring(0, 3).toLocaleUpperCase()}</div>`;
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
        let day = __schWeekdays[date.getDay()];

        tmp += `<div class="sch-day-time-container" id="sch-day-time-container-${i}">`;

        $.each(settings.timeTemplates[day], function (_, time) {
          let classNames = [];
          let styles = [];
          let title = date.printDate() + " : " + printTime(time) + " " + `(${day.capitalize()})`;

          let slotHour = Number.parseInt(time);
          let isBefore = isBeforeSlot(date, slotHour);

          if (isBefore) {
            classNames.push("is-not-toggleable");
            title = "This time slot is closed";
          }

          if (!isBefore && isSelectedTime(day, date, time)) {
            classNames.push("selected");
          }

          if (!isBefore && isBookedTime(day, date, time)) {
            title =
              day.capitalize() +
              " " +
              date.printDate() +
              " (" +
              printTime(time) +
              ") is booked.\n * This can no longer be changed.";
            classNames.push("booked");
            styles.push(`color: ${settings.colors.bookedSlot.text}`);
            styles.push(`background-color: ${settings.colors.bookedSlot.bg}`);
          }

          tmp += `<div class='time-slot'>`;
          tmp += `<div class="sch-available-time ${classNames.join(" ")}" style="${styles.join(
            ";"
          )}" title="${title}" data-toggle="tooltip"`;
          tmp += `data-time="${time}" data-date="${date.printDate()}" data-day="${day}"`;
          tmp += ` >${printTime(time)}</div>`;
          tmp += `</div>`;
        });
        tmp += `<div style="clear:both;"></div>`;
        tmp += `</div>`;
      }
      var ret = `<div id="sch-available-time-container">` + tmp + `</div>`;
      return ret;
    };

    let renderFooterSection = function () {
      // let selectedDateContainer = `

      // <p class="sch-footer-title">Selected dates / times:</p>
      // <ul id="sch-selected-date-container"></ul>

      // `;

      let submissionContainer = `
        <button id="sch-submit-button" disabled>Submit</button>
        <span id="sch-submit-message"></span>
        `;

      let footerContainer = `
        <div id="sch-footer-container">
        <div id="sch-footer-left">
        `;

      // footerContainer = `
      // ${selectedDateContainer}
      // `;

      footerContainer += `
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
          var [_, date, time] = date.split("|");
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
      console.log("render");
      let ret = `<div id="sch-week-container">`;
      ret += instance.getMonthTitle();
      ret += instance.getDatesHeader();
      ret += instance.getTimeSlots();
      ret = ret + `</div>`;
      instance.html(ret);
      renderFooterSection();
    };

    this.getSelectedSlots = function () {
      let _slots = [];

      $.each(selections, function (_, item) {
        if (_slots.indexOf(item) == -1) {
          _slots.push(item);
        }
      });
      return _slots;
    };

    instance.on("click", ".sch-available-time:not(.booked)", function (e) {
      console.log("auto apply toggle");
      var day = $(this).data("day");
      var date = $(this).data("date");
      var time = $(this).data("time");

      if (!isBeforeSlot(date, Number.parseInt(time))) {
        selectDay(day, date, time);
        instance.hasModified = true;
        render();
      }
    });

    instance.on("click", "#sch-auto-apply-toggle", function (e) {
      if (currentPage > 0) {
        let idx = autoAppliedPages.indexOf(currentPage);
        if (idx === -1) {
          applySlots(currentPage);
          autoAppliedPages.push(currentPage);
        } else {
          applySlots(currentPage);
          autoAppliedPages.splice(idx, 1);
        }
        render();
      }
    });

    instance.on("click", "#sch-submit-button", function (e) {
      if (instance.hasModified) {
        if ($.isFunction(onSubmit)) {
          let isNewAppointment = isNull(settings.availability) || isEmptyObj(settings.availability);
          asyncIt(onSubmit.call(this, instance.getSelectedSlots(), settings.availability, isNewAppointment))
            .then((value) => {
              onSuccess(value);
              instance.hasModified = false;
              render();
              $("#sch-submit-message").text("Timeslots Successfully saved.");
            })
            .catch(onError);
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

    for (let index = 0; index < scheduleDays; index++) {
      days.push(settings.startDate.addDays(index));
    }

    if ($.isFunction(loadTimes)) {
      asyncIt(loadTimes.call(this, ...arguments))
        .then((timeTemplates) => {
          settings.timeTemplates = timeTemplates ?? {};
          return asyncIt(onLoadAvailablities.call(this, ...arguments));
        })
        .then((availableSlots) => {
          settings.availability = availableSlots;
        })
        .catch(onError)
        .finally(() => {
          if (settings.availability != null && settings.availability.Availability != null) {
            updateAvailability(settings.availability.Availability);
            updateBooked(settings.availability.Appointments);
          }
          render();
        });
    }
  };
})(jQuery);
