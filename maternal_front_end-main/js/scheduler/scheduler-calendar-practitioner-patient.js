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
    //availability: {},
    availability: {},
    colors: {
      bookedSlot: {
        text: "#585858",
        bg: "#8e8e",
      },
    },
  };

  function setBackDrop() {
    if (!$("#ajax-backdrop").length) {
      $("body").append(`<div id="ajax-backdrop">
        <div id="ajax-backdrop-content">
        <img src="images/ajax-loader.gif">
        <div id="ajax-loader-text">Processing...</div>
        </div>
      </div>`);
    }
  }

  function removeBackDrop() {
    $("#ajax-backdrop").remove();
  }

  // patient appointment booking
  $.fn.patientBookingCalendar = function (opts) {
    let selection = null;
    let reason = "";
    let meet = null;
    let instance = this;
    let settings = $.extend({}, defaults, opts);
    let bookingDays = 30;
    let days = [];
    let bookedSlots = [];
    let pagesCount = Math.ceil(bookingDays / 7);
    let currentPage = 0;
    let isNewAppointment = null;

    var getDates = (pageIndex) => {
      let startIndex = Math.max(0, pageIndex) * 7;
      return days.slice(startIndex, startIndex + 7);
    };

    var hasPrev = () => currentPage > 0;
    var hasNext = () => currentPage < pagesCount - 1;

    var onLoadSlots = settings.onLoadSlots;
    var onNewAppointmentCheck = settings.onNewAppointmentCheck;
    var onSubmit = settings.onSubmit;
    var onError = settings.onError;
    var onSuccess = settings.onSuccess;

    let slotInfo = {
      selectedDate: [],
      availabilityId: null,
      timeId: null,
      practitionerId: null,
      isNewAppointment: null,
    };

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

    var selectDay = (day, date, time) => {
      let dateTime = dayTimeString(day, date, time);
      if (selection === dateTime) {
        selection = null;
      } else {
        selection = dateTime;
      }
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

    this.hasModified = () => {
      reason = $("#sch-reschedule-textbox").val() ?? "";
      return !isEmpty(selection) && !isEmpty(meet) && !isEmpty(reason);
    };

    this.getDoctorInfo = function () {
      var ret = ``;
      ret += `<div class="sch-practitioner-title">
      <span><b>Your Availability</b></span>
      </div>`;

      return ret;
    };
    this.getMonthTitle = function () {
      var ret = ``;

      let startMonth = settings.startDate.getMonthName().substring(0, 3).toUpperCase();
      let endMonth = settings.startDate.getMonthNextName().substring(0, 3).toUpperCase();
      let year = settings.startDate.getFullYear();

      ret += `<div class="sch-calendar-head">`;
      ret += `<div class="sch-nav-btn sch-nav-prev-week" ${!hasPrev() ? "disabled" : ""} title="Previous 7 days">`;
      ret += `<i class="material-icons">chevron_left</i>Previous`;
      ret += `</div>`;
      ret += `<div class="sch-month-title-text">`;
      ret += `${startMonth} - ${endMonth} ${year}`;
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

        let dayExists = slotInfo.selectedDate.findIndex((x) => x.split("|")[1] === date.printDate()) !== -1;

        tmp += `<div class="sch-day-time-container" id="sch-day-time-container-${i}">`;

        if (dayExists) {
          let times = [];

          $.each(slotInfo.selectedDate, function (_, slot) {
            const [slotDay, slotDate, slotTime] = slot.split("|");
            if (slotDate === date.printDate()) {
              times.push(slotTime);
            }
          });
          $.each(times.ascending(), function (_, slotTime) {
            let classNames = [];
            let styles = [];
            let title = date.printDate() + " : " + printTime(slotTime) + " " + `(${day.capitalize()})`;

            let slotHour = Number.parseInt(slotTime);
            let isBefore = isBeforeSlot(date, slotHour);
            let isSelected = selection == dayTimeString(day, date.printDate(), slotTime);

            if (isBefore) {
              classNames.push("is-not-toggleable");
              title = "This time slot is closed";
            }

            if (!isBefore && isSelected) {
              classNames.push("selected");
            }

            if (!isBefore && isBookedTime(day, date, slotTime)) {
              title =
                day.capitalize() +
                " " +
                date.printDate() +
                " (" +
                printTime(slotTime) +
                ") is booked.\n * This can no longer be changed.";
              classNames.push("booked");
              styles.push(`color: ${settings.colors.bookedSlot.text}`);
              styles.push(`background-color: ${settings.colors.bookedSlot.bg}`);
            }

            tmp += `<div class='time-slot'>`;
            tmp += `<div class="sch-available-time ${classNames.join(" ")}" style="${styles.join(
              ";"
            )}" title="${title}" data-toggle="tooltip"`;
            tmp += `data-time="${slotTime}" data-date="${date.printDate()}" data-day="${day}">`;
            tmp += `${printTime(slotTime)}</div>`;
            tmp += `</div>`;
          });

        } else {
          tmp += `<div class='time-slot'>`;
          tmp += `<div class='time-slot-empty'>`;
          tmp += `No Slots`;
          tmp += `</div>`;
          tmp += `</div>`;
        }
        tmp += `<div style="clear:both;"></div>`;
        tmp += `</div>`;
      }
      var ret = `<div id="sch-available-time-container">` + tmp + `</div>`;
      return ret;
    };

    let renderFooterSection = function () {
      let virtualChecked = meet === "virtual" ? "checked" : "";
      let inPersonChecked = meet === "in_person" ? "checked" : "";

      let meetSelectionContainer = `
      <div class="textbox-container">
      <textarea class="form-control" id="sch-reschedule-textbox" rows="3" placeholder="Please reason for reschedule">${reason}</textarea>
      </div>
      <p class="sch-footer-title">Select your Meeting</p>
      <ul class="p-0" id="sch-meet-selecion-container">
        <div class="form-check pr-2"> 
          <input class="form-check-input" type="radio" name="meet" value="virtual" id="meet_virtual" ${virtualChecked}/> 
          <label class="form-check-label" for="meet_virtual">Virtual</label>
        </div>
        <div class="form-check">
          <input class="form-check-input" type="radio" name="meet" value="in_person" id="meet_in_person" ${inPersonChecked}/> 
          <label class="form-check-label" for="meet_in_person">In Person</label>
        </div>
      </ul>
      
      `;

      let submissionContainer = `
      <button id="sch-submit-button" disabled>Submit</button>
      <span id="sch-submit-message"></span>
      `;

      let footerContainer = `
      <div id="sch-footer-container">
      <div id="sch-footer-left">
      `;
      if (isNewAppointment) {
        footerContainer += `* <b>Note</b> first appointment will be virtual appointment.`;
        meet = "virtual";
      } else if (!isNewAppointment && selection != null) {
        footerContainer += meetSelectionContainer;
      }

      footerContainer += `
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
      let ret = instance.getDoctorInfo();
      ret += `<div id="sch-week-container">`;
      ret += instance.getMonthTitle();
      ret += instance.getDatesHeader();
      ret += instance.getTimeSlots();
      ret = ret + `</div>`;
      instance.html(ret);
      renderFooterSection();
    };
    render();

    instance.on("click", ".sch-available-time:not(.booked)", function (e)  {
      var day = $(this).data("day");
      var date = $(this).data("date");
      var time = $(this).data("time");

      let isBefore = isBeforeSlot(date, time);
      if (!isBefore) {
        selectDay(day, date, time);
        render();
      }
    });

    instance.on("click", "input[name='meet']", function (e) {
      meet = $("input[name='meet']:checked").val();
      render();
    });

    instance.on("click", "#sch-submit-button", function (e) {
      if (instance.hasModified) {
        if ($.isFunction(onSubmit)) {
          let [day, date, time] = selection.split("|");
          meet = $("input[name='meet']:checked").val();

          let isVirtual = meet === "virtual" || isNewAppointment;

          let json = {
            date: date,
            time: time,
            day: day,
            meet,
            timeslot: selection,
            slotInfo,
          };

          return asyncIt(onSubmit.call(this, json, isVirtual))
            .then((value) => {
              if (value != null) {
                selection = null;
                if (onSuccess) {
                  onSuccess.call(this, value, isVirtual);
                }
                instance.hasModified = false;
                render();
                $("#sch-submit-message").text("Timeslots Successfully saved.");
              }
            })
            .catch((error) => {
              if (onError) {
                onError.call(this, error, isVirtual);
              }
            });
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

    instance.on("change", "#sch-reschedule-textbox", function (e) {
      reason = $("#sch-reschedule-textbox").val() ?? "";
      render();
    });

    for (let index = 0; index < bookingDays; index++) {
      days.push(settings.startDate.addDays(index));
    }

    if ($.isFunction(onLoadSlots)) {
      asyncIt(onLoadSlots.call(this, ...arguments))
        .then((info) => {
          slotInfo = info ?? slotInfo;
          return asyncIt(onNewAppointmentCheck.call(this, ...arguments));
        })
        .then((isNew) => {
          isNewAppointment = isNew ?? isNewAppointmentCheck;
        })
        .catch(onError)
        .finally(() => {
          if (slotInfo.appointment instanceof Array) {
            updateBooked(slotInfo.appointment);
          } else if (slotInfo.appointment != null && typeof slotInfo.appointment === "string") {
            updateBooked([slotInfo.appointment]);
          }
          render();
        });
    }
  };
})(jQuery);
