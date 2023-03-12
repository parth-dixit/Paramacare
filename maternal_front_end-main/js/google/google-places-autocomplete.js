$(function () {
  const input = document.getElementById("add1");

  const options = {
    componentRestrictions: { country: "us" },
    fields: ["formatted_address", "geometry", "name", "address_components"],
    types: ["geocode"],
  };
  const autocomplete = new google.maps.places.Autocomplete(input, options);
  google.maps.event.addListener(autocomplete, "place_changed", function () {
    const place = autocomplete.getPlace();
    let address_line_1 = [];
    let city = [];
    let state = "";
    let postal_code = "";

    let lat = place.geometry.location.lat();
    let long = place.geometry.location.lng();

    place.address_components.forEach((component) => {
      if (component.types.includes("street_number")) {
        address_line_1.push(component.long_name);
      }

      if (component.types.includes("route")) {
        address_line_1.push(component.long_name);
      }

      if (component.types.includes("neighborhood")) {
        address_line_1.push(component.long_name);
      }

      if (component.types.includes("administrative_area_level_1")) {
        state = component.long_name;
      }

      if (component.types.includes("locality")) {
        city.push(component.long_name);
      }

      if (component.types.includes("postal_code")) {
        postal_code = component.long_name;
      }
    });

    const address_line_1_string = $.unique(address_line_1).join(",");
    const city_string = $.unique(city).join(",");
    $("#add1").val(address_line_1_string);
    $("#add1").attr("title", address_line_1_string);

    $("#zip").val(postal_code);
    $("#city").val(city_string);
    $("#state").val(state);
    $("#g-postal-code").val(postal_code);
    $("#g-lat-long").val(lat + "," + long);
  });
});
