$(document).ready(function() {
	attachEventHandlers();
});

/**
 * attach the event handlers needed to handle the client events
 * 
 */
function attachEventHandlers() {
	$("#city-name").keypress(function(event) {

		searchCities(event);
	});
	$(".typeahead .list-group").on("click", ".list-group-item",
			function(event) {
				selectCity(event);
			});

	$(".weather-cards").on("click", ".refresh-weather", function(event) {
		refresh(event);
	});
}

/**
 * This function creates the typeahead for the location that user is searching.
 * Makes a call to an api that gives city name based on the user search and then
 * populates the results in the typeahead
 * 
 * @param event
 *            the type event
 */
function searchCities(event) {
	var url = "https://api.teleport.org/api/cities/?search="
			+ event.target.value;
	$.ajax({
		url : url,
		type : "GET",
		crossDomain : true,
		dataType : "json",
		success : function(response) {
			$(".list-group").empty();
			data = response["_embedded"]["city:search-results"]
			$.each(data, function(index) {

				var city = data[index]["matching_full_name"];
				$(".list-group").append(
						'<li class="list-group-item">' + city + '</li>');

				if (index == 2) {
					return false;
				}
			});
			$(".typeahead").css("display", "inline-block");
		},
		error : function(xhr, status) {
			console.log("error");
		}
	});

}

/*
 * select the city and get the weather
 */
function selectCity(event) {
	$('#city-name').val($(event.target).text());
	$(".list-group").empty();
	$(".source-hidden").empty().hide();
	getWeather($(event.target).text(), false);

}

/*
 * make call to the servelet on the server to get the json response to populate
 * the weather
 */
function getWeather(selectedOption, refresh) {
	options = selectedOption.split(',');
	$.ajax({
		url : "WeatherServlet?city=" + options[0].trim() + "&province="
				+ options[1].trim() + "&country=" + options[2].trim()
				+ "&refresh=" + refresh,
		type : "GET",
		dataType : "json",
		success : function(response) {
			weatherData = response;
			populateWeatherCards(weatherData, selectedOption);
		},
		error : function(xhr, status) {
			return null;
		}
	});
}

/*
 * populate weather cards by parsing the JSON
 */
function populateWeatherCards(weatherData, location) {
	apiuxData = weatherData['apiux-data']
	darkSkyData = weatherData['dark-sky-data']
	openWeatherData = weatherData['open-weather-data']

	$(".source-1").append("<h2>Apiux Weather Service</h2>").fadeIn(1000);
	$(".source-1").append(
			getCard(apiuxData['temperature'], apiuxData['wind-speed'],
					apiuxData['condition'], location,
					openWeatherData['last-updated'], openWeatherData['icon']))
			.fadeIn(4000);

	$(".source-2").append("<h2>Dark Sky Weather Service</h2>").fadeIn(1000);
	$(".source-2").append(
			getCard(darkSkyData['temperature'], darkSkyData['wind-speed'],
					darkSkyData['condition'], location,
					openWeatherData['last-updated'], openWeatherData['icon']))
			.fadeIn(4000);

	$(".source-3").append("<h2>Open Weather Map Service</h2>").fadeIn(1000);
	$(".source-3").append(
			getCard(openWeatherData['temperature'],
					openWeatherData['wind-speed'],
					openWeatherData['condition'], location,
					openWeatherData['last-updated'], openWeatherData['icon']))
			.fadeIn(4000);
}

function getCard(temerature, wind, condition, location, lastUpdated, icon) {
	return '<div class="card">\
	<div class="card-body">\
	<h5 class="card-title">'
			+ location
			+ '</h5>\
	<div class="weather-icon-div">\
		<i class="weather-icon wi '
			+ icon
			+ '"></i>\
	</div>\
	<div class="weather-temperature">\
		<span style="font-size: 80px;">'
			+ temerature
			+ '</span><sup\
			style="font-size: 50px;">℃</sup>\
	</div>\
	<div class="weather-wind">\
		<h5>Wind Speed: '
			+ wind
			+ 'km/h</h5>\
	</div>\
	<div class="weather-condition">\
		<h5>Condition Summary: '
			+ condition
			+ '</h5>\
	</div>\
	<div class="weather-last-updated-div">\
	<span>Last Updated: '
			+ lastUpdated
			+ '  <a class="refresh-weather" data-city="'
			+ location + '">Referesh</a></span>\
	</div>\
	</div>\
	</div>'

}

/*
 * Force fetch from the weather service
 */
function refresh(event) {
	var city = $(event.target).data("city");
	$('#city-name').val(city);

	$(".list-group").empty();

	$(".source-hidden").empty().hide();

	getWeather(city, true);
}
