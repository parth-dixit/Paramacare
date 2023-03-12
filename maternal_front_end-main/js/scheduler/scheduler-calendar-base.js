var __schTimes = ["7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"];

let __schWeekdays = ["sun", "mon", "tue", "wed", "thu", "fri", "sat"];

let __schWeekdaysNames = {
  sun: "sunday",
  mon: "monday",
  tue: "tuesday",
  wed: "wednesday",
  thu: "thursday",
  fri: "friday",
  sat: "saturday",
};

let __schMonths = [
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
function isEmpty(value) {
  if (value == null || value == undefined || value == "") return true;
  else if (typeof value == "string") return value.trim() == "";
  else if (typeof value == "object") return isEmptyObj(value);
  else return false;
}
function getQueryParam(name) {
  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
    results = regex.exec(location.search);
  return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

// check if async function or not
function isAsync(callback) {
  return callback.constructor.name === "AsyncFunction";
}

function isNull(value) {
  return value == null || value == undefined;
}

function isEmptyObj(value) {
  return Object.keys(value).length === 0 && value.constructor === Object;
}

// convert sync function to async function
async function asyncIt(callback, errMessage) {
  if (!isAsync(callback)) return callback;
  else
    return new Promise(async (resolve, reject) => {
      let response = await callback;
      if (response != null) return resolve(response);
      else reject(Error(errMessage));
    });
}

(function ($) {
  String.prototype.capitalize = function () {
    if (this.length == 0) return this;
    return this.charAt(0).toUpperCase() + this.slice(1);
  };

  Array.prototype.ascending = function () {
    return this.sort((a, b) => a - b);
  };

  Array.prototype.descending = function () {
    return this.sort((a, b) => b - a);
  };

  Date.prototype.addDays = function (days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
  };

  Date.prototype.getMonthName = function () {
    return __schMonths[this.getMonth()];
  };

  Date.prototype.getMonthNextName = function () {
    let index = this.getMonth() < __schMonths.length - 1 ? this.getMonth() + 1 : 0;
    return __schMonths[index];
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
    return `${day}|${date}|${time}`;
  };

  printTime = (hour) => {
    hour = Number.parseInt(hour);
    if (hour === 12) {
      return `${hour} PM`;
    } else if (hour > 12) {
      return `${hour - 12} PM`;
    } else {
      return `${hour} AM`;
    }
  };
})(jQuery);
