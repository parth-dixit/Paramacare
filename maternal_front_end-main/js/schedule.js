(function ($) {
    Date.prototype.addDays = function (days) {
      var date = new Date(this.valueOf());
      date.setDate(date.getDate() + days);
      return date;
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
  
    dateTimeString = (date, time) => {
      return `${date.printDate()}|${timeFormat(time)}`;
    };
  
    timeFormat = (time) => {
      let [hour, minute] = time.split(":");
      hour = hour.length < 2 ? "0" + hour : hour;
      return `${hour}:${minute}`;
    };
  
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
      availability: {
        monday: [],
        tuesday: [],
        wednesday: [],
        thursday: [],
        friday: [],
        saturday: [],
        sunday: [],
      },
    };
  
    $.fn.scheduler = function (opts) {
      let selections = [];
      let oldSelections = [];
      let instance = this;
      var settings = $.extend({}, defaults, opts);
  
      var onSubmit = settings.onSubmit;
      this.hasModified = false;
  
      var selectDay = function (date, time) {
        var tmp = dateTimeString(date, timeFormat(time));
        let idx = selections.indexOf(tmp);
        if (idx !== -1) {
          selections.splice(idx, 1);
        } else {
          selections.push(tmp);
        }
      };
  
      for (let i = 0; i < weekdays.length; i++) {
        var date = settings.startDate.addDays(i);
        var times = settings.availability[weekdays[date.getDay()]] ?? [];
  
        $(times).each(function (_, time) {
          selectDay(date, timeFormat(time));
        });
      }
  
      this.getMonthTitle = function () {
        var ret = ``;
        let month = months[settings.startDate.getMonth()]
          .substring(0, 3)
          .toUpperCase();
        let year = settings.startDate.getFullYear();
        ret += `<div class="sch-month-title">`;
        ret += `${month} ${year}`;
        ret += `</div>`;
        return ret;
      };
  
      this.getDatesHeader = function () {
        var tmp = ``;
        for (i = 0; i < weekdays.length; i++) {
          var dateInstance = settings.startDate.addDays(i);
         // console.log(dateInstance);
          let day = weekdays[dateInstance.getDay()];
         // console.log(day);
          let date = dateInstance.getDate();
         // console.log(date);
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
        //let day = weekdays[date.getDay()];
        //console.log(day);
        for (i = 0; i < weekdays.length; i++) {
          var date = settings.startDate.addDays(i);
          let day = weekdays[date.getDay()];
         // console.log(day);
          tmp += `<div class="sch-day-time-container" id="sch-day-time-container-${i}">`;
  
          $.each(settings.timeTemplates[day], function (_, time) {
            time = timeFormat(time);
            let selectedClass = selections.includes(dateTimeString(date, time))
              ? "selected"
              : "";
            tmp += `<div class='time-slot'>`;
            tmp += `<div class="sch-available-time ${selectedClass}" data-time="${time}" data-date="${date.printDate()}">${time}</div>`;
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
            var date = date.split("|");
            var dateInstance = new Date(date[0]);
            var time = date[1];
            element.append(`
                <li class="sch-selected-date">
                  <div class="sch-selected-date-date">${dateInstance.printDate()}</div> 
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
        let _json = {
          monday: [],
          tuesday: [],
          wednesday: [],
          thursday: [],
          friday: [],
          saturday: [],
          sunday: [],
        };
  
        $.each(selections, function (_, item) {
          var dateTime = item.split("|");
          var dayIndex = new Date(dateTime[0]).getDay();
          var time = timeFormat(dateTime[1]);
          let idx = _json[weekdays[dayIndex]].indexOf(time);
          if (idx === -1) {
            _json[weekdays[dayIndex]].push(time);
          }
        });
        return _json;
      };
  
      instance.on("click", ".sch-available-time", function (e) {
        var date = $(this).data("date");
        console.log(date);
        var time = $(this).data("time");
        console.log(time);
        selectDay(new Date(date), timeFormat(time));
        //console.log(selectDay(new Date(date), timeFormat(time)));
        instance.hasModified = true;
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
    };
  })(jQuery);
  