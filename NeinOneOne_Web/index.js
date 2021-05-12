// set up Express
var express = require('express');
var app = express();

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// Set up ejs
app.use(express.static(__dirname + '/public'));
app.set('views', __dirname + '/public');
app.engine('html', require('ejs').renderFile);
app.set('view engine', 'html');

const http = require('http');

// import the Resource class from Resource.js
var Resource = require('./Resource.js');
var Suggestion = require('./Suggestion.js');

/***************************************/

// endpoint for creating a new Resource
// this is the action of the "create new Resource" form


app.use('/create', (req, res) => {

	var searchZipcode = req.body.zipcode.trim();

	//if the user entered zipcode information
	if (searchZipcode) {

		var url = 'http://api.positionstack.com/v1/forward?access_key=c19118447bc587fb3352ef92eeddd47c&query=zipcode:'
			+ searchZipcode + '&country_code:USA';
		console.log(url);
		http.get(url, (resp) => {
			let data = '';

			// A chunk of data has been received.
			resp.on('data', (chunk) => {
				data += chunk;
			});

			// The whole response has been received. Print out the result.
			resp.on('end', () => {

				var locations = JSON.parse(data).data;

				// if no location with given zipcode is found, print out message and return
				if (locations.length == 0) {
					res.type('html').status(200);
					res.write('Zipcode not found.');
					res.end();
					return;
				}

				var lat = locations[0].latitude;
				var long = locations[0].longitude;
				createNewResource(req, res, lat, long);
			});

		}).on("error", (err) => {
			console.log("Error: " + err.message);
		});
	} else {
		createNewResource(req, res, 0, 0);
	}
}
);

// construct the Resource from the form data which is in the request body
function createNewResource(request, response, lat, long) {
	var newResource = new Resource({
		name: request.body.name.trim(),
		website: request.body.website.trim(),
		phone: request.body.phone.trim(),
		description: request.body.description.trim(),
		location: request.body.street.trim() + ' ' + request.body.city.trim() + ', '
			+ request.body.state.trim() + ' ' + request.body.zipcode.trim(),
		zipcode: request.body.zipcode.trim(),
		latitude: lat,
		longitude: long
	});
	// save the Resource to the database
	newResource.save((err) => {
		if (err) {
			response.type('html').status(200);
			response.write('uh oh: ' + err);
			console.log(err);
			response.end();
		}
		else {
			// display the "successfully created" message
			response.send('Successfully added ' + newResource.name + ' to the database');
		}
	});
}

app.post('/suggest', (req, res) => {
	var newSuggestion = new Suggestion({
		name: req.body.name,
		website: req.body.website,
		phone: req.body.phone,
		description: req.body.description,
	});

	// save the Resource to the database
	newSuggestion.save((err) => {
		if (err) {
			res.type('html').status(200);
			res.write('uh oh: ' + err);
			console.log(err);
			res.end();
		}
		else {
			// display the "successfully created" message
			res.send('Successfully added suggestion: ' + newSuggestion.name + ' to the database');
			console.log(newSuggestion.website)
		}
	});
}
);


// endpoint for showing all the Resources
app.use('/list', (req, res) => {

	// find all the Resource objects in the database
	Resource.find({}, (err, resourceList) => {
		if (err) {
			res.type('html').status(200);
			console.log('error: ' + err);
			res.write(err);
		}
		else {
			if (resourceList.length == 0) {
				res.type('html').status(200);
				res.write('There are no resources.');
				res.end();
				return;
			}
			else {
				res.type('html').status(200);
				res.write('Here are all the resources in the database:');
				res.write('<ol>');
				// show all the resources
				resourceList.forEach((resource) => {
					res.write('<li>' + resource.name);
					res.write('<ul>');
					res.write('<li>Description: ' + resource.description + '</li>'
						+ '<li>Phone Number: ' + resource.phone + '</li>'
						+ '<li>Website: ' + resource.website + '</li>'
						+ '<li>Location: ' + resource.location + '</li>'
						+ '<li>Latitude: ' + resource.latitude + '</li>'
						+ '<li>Longitude: ' + resource.longitude
						+ '<br>');
					res.write('</ul>');
				});
				res.write('</ol>');
				res.end();
			}
		}
	});
});

app.use('/suggestlist', (req, res) => {

	// find all the Resource objects in the database
	Suggestion.find({}, (err, suggestList) => {
		if (err) {
			res.type('html').status(200);
			console.log('error: ' + err);
			res.write(err);
		}
		else {
			if (suggestList.length == 0) {
				res.type('html').status(200);
				res.write('There are no resources.');
				res.end();
				return;
			}
			else {
				res.type('html').status(200);

				res.render('suggested_list.html', { suggestionsArr: suggestList });
			}
		}
	});
});

// endpoint for accessing data via the web api
// to use this, make a request for /api to get an array of all Resource objects
// or /api?name=[whatever] to get a single object
app.use('/api', (req, res) => {

	// construct the query object
	var queryObject = {};
	if (req.query.name) {
		// if there's a name in the query parameter, use it here
		queryObject = { "name": req.query.name };
	}

	Resource.find(queryObject, (err, resources) => {
		console.log(resources);
		if (err) {
			console.log('uh oh' + err);
			res.json({});
		}
		else if (resources.length == 0) {
			// no objects found, so send back empty json
			res.json({});
		}
		else if (resources.length == 1) {
			var resource = resources[0];
			// send back a single JSON object
			res.json({
				"name": resource.name, "website": resource.website,
				"phone": resource.phone, "description": resource.description,
				"location": resource.location, "zipcode": resource.zipcode,
				"latitude": resource.latitude, "longitude": resource.longitude
			});
		}
		else {
			// construct an array out of the result
			var returnArray = [];
			resources.forEach((resource) => {
				returnArray.push({
					"name": resource.name, "website": resource.website,
					"phone": resource.phone, "description": resource.description,
					"location": resource.location, "zipcode": resource.zipcode,
					"latitude": resource.latitude, "longitude": resource.longitude
				});
			});
			// send it back as JSON Array
			res.json(returnArray);
		}

	});
});

app.use('/update', (req, res) => {
	var filter = { 'name': req.query.name };
	var action = null;

	if (req.query.website != null && req.query.phone != null && req.query.description) {
		action = { '$set': { 'website': req.query.website, 'phone': req.query.phone, 'description': req.query.description } };
	} else if (req.query.website != null && req.query.phone != null) {
		action = { '$set': { 'website': req.query.website, 'phone': req.query.phone } };
	} else if (req.query.website != null && req.query.description != null) {
		action = { '$set': { 'website': req.query.website, 'description': req.query.description } };
	} else if (req.query.phone != null && req.query.description != null) {
		action = { '$set': { 'phone': req.query.phone, 'description': req.query.description } };
	} else if (req.query.website != null) {
		action = { '$set': { 'website': req.query.website } };
	} else if (req.query.phone != null) {
		action = { '$set': { 'phone': req.query.phone } };
	} else if (req.query.description != null) {
		action = { '$set': { 'description': req.query.description } };
	}

	if (action != null) {
		Resource.findOneAndUpdate(filter, action, (err, orig) => {
			if (err) {
				res.json({ 'status': err });
			}
			else if (!orig) {
				res.json({ 'status': 'no resource' });
			}
			else {
				res.json({ 'status': 'success' });
			}
		});
	} else {
		res.json({ 'status': 'no updates performed' });
	}
});

// endpoint for searching for a keyword
app.use('/search', (req, res) => {

	// construct the query object
	var queryObject = {};
	var key = '';
	if (req.body.keyword) {
		// if there's a keyword in the body parameter, use it here
		queryObject = { "keyword": req.body.keyword };
		key = req.body.keyword;
	}
	else if (req.query.keyword) {
		// if there's a keyword in the query parameter, use it here
		queryObject = { "keyword": req.query.keyword };
		key = req.query.keyword;
	}

	// construct the location query object
	var locationQueryObject = {};
	var zip = '';
	if (req.body.location) {
		// if there's a location in the body parameter, use it here
		locationQueryObject = { "location": req.body.location };
		zip = req.body.location;
	}
	else if (req.query.location) {
		// if there's a location in the query parameter, use it here
		locationQueryObject = { "location": req.query.zipcode };
		zip = req.query.zipcode;
	}

	// convert the location entered by admin into latitude and longitude
	var url = 'http://api.positionstack.com/v1/forward?access_key=c19118447bc587fb3352ef92eeddd47c&query=zipcode:'
		+ zip + '&country_code:USA';

	// HTTP request to get location from the  url
	http.get(url, (resp) => {
		let data = '';

		// A chunk of data has been received.
		resp.on('data', (chunk) => {
			data += chunk;
		});

		// The whole response has been received. Print out the result.
		resp.on('end', () => {
			var locations = JSON.parse(data).data;

			// if no location with given zipcode is found, print out message and return
			if (locations.length == 0) {
				res.type('html').status(200);
				res.write('Zipcode not found.');
				res.end();
				return;
			}

			// Use the first location from the array as the location we want
			var lat = locations[0].latitude;
			var long = locations[0].longitude;


			// find all the Resource objects in the database
			Resource.find({}, (err, resourceList) => {
				if (err) {
					res.type('html').status(200);
					console.log('error: ' + err);
					res.write(err);
				}
				else {
					if (resourceList.length == 0) {
						res.type('html').status(200);
						res.write('There are no resources.');
						res.end();
						return;
					}
					else {
						res.type('html').status(200);

						// If key and zip are both empty
						if (key == '' && zip == '') {
							res.write('Please enter a search term.');
							res.end();
						}

						// Search based on only keyword - if zip is empty but not key 
						else if (zip == '' && key != '') {
							res.write("Here are all the resources in the database containing '" + key + "':");
							res.write('<ol>');
							var keywordCount = 0;

							resourceList.forEach((resource) => {
								// check if name or description contain keyword
								if (resource.name.includes(key) || resource.description.includes(key)) {
									keywordCount++;

									res.write('<li>' + resource.name);
									res.write('<ul>');
									res.write('<li>Description: ' + resource.description + '</li>'
										+ '<li>Phone Number: ' + resource.phone + '</li>'
										+ '<li>Website: ' + resource.website + '</li>'
										+ '<br>');
									res.write('</ul>');
								}
							});
							if (keywordCount == 0) {
								res.write("There are no resources containing '" + key + "'.");
							}
							res.write('</ol>');
							res.end();
						}

						// Search based on only location - if key is empty but not zip 
						else if (key == '' && zip != '') {
							var locationList = getNearbyLocations(resourceList, lat, long);

							res.write("Here are all the resources in the database containing '" + zip + "':");
							res.write('<ol>');

							locationList.forEach((location) => {
								var resource = location.resource;

								res.write('<li>' + resource.name);
								res.write('<ul>');
								res.write('<li>Description: ' + resource.description + '</li>'
									+ '<li>Phone Number: ' + resource.phone + '</li>'
									+ '<li>Website: ' + resource.website + '</li>'
									+ '<li>Zipcode: ' + resource.zipcode + '</li>'
									+ '<li>Lat, Long: ' + resource.latitude + ', ' + resource.longitude + '</li>'
									+ '<br>');
								res.write('</ul>');
							});

							if (locationList.length == 0) {
								res.write("There are no resources containing '" + zip + "'.");
							}
							res.write('</ol>');
							res.end();
						}

						// Search based on keyword AND location (not OR) - if both keyword and location are given 
						else {
							var locationList = getNearbyLocations(resourceList, lat, long);

							// If there are no nearby locations
							// if (locationList.length == 0) {
							// Expand the radius or only search by keyword?
							// }

							res.write("Here are all the resources in the database for '" + zip + "'and containing '" + key + "':");
							res.write('<ol>');
							var count = 0;

							locationList.forEach((location) => {
								var resource = location.resource;

								if (resource.name.includes(key) || resource.description.includes(key)) {
									count++;

									res.write('<li>' + resource.name);
									res.write('<ul>');
									res.write('<li>Description: ' + resource.description + '</li>'
										+ '<li>Phone Number: ' + resource.phone + '</li>'
										+ '<li>Website: ' + resource.website + '</li>'
										+ '<li>Zipcode: ' + resource.zipcode + '</li>'
										+ '<li>Lat, Long: ' + resource.latitude + ', ' + resource.longitude + '</li>'
										+ '<br>');
									res.write('</ul>');
								}
							});

							if (count == 0) {
								res.write("There are no resources containing for '" + zip + "'and containing '" + key + "'.");
								res.end();
							}
							res.write('</ol>');
							res.end();
						}
					}
				}
			});
		}); // end
	}).on("error", (err) => {
		console.log("Error: " + err.message);
	});
});

// Function to return a list of resources that are nearby the zipcode being searched for 
function getNearbyLocations(resourceList, lat, long) {

	// A list of maps that contains resources and their distance from the zipcode the admin entered
	var locationList = [];

	// check if resources are in a 10 mile radius
	resourceList.forEach((resource) => {

		// Skip resources without location
		if (typeof resource.latitude == 'undefined' || typeof resource.longitude == 'undefined' || resource.latitude == null || resource.longitude == null || resource.latitude == 0 || resource.longitude == 0)
			return true;

		var d = calculateGreatCircleDist(lat, long, resource.latitude, resource.longitude);

		if (d <= 10) {
			// Add resource and its distance to list 
			locationList.push({ 'resource': resource, 'distance': d });
		}
	});

	// Sort locationList by distance
	locationList.sort((a, b) => (a.distance > b.distance) ? 1 : ((b.distance > a.distance) ? -1 : 0));

	return locationList;
}

// Calculate the great circle distance between two locations
function calculateGreatCircleDist(lat1, long1, lat2, long2) {
	// https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula 

	const radius = 3956.0; 			// Radius of the earth in miles
	var lat = degreeToRadian(lat2 - lat1);
	var long = degreeToRadian(long2 - long1);
	var lat1rad = degreeToRadian(lat1);
	var lat2rad = degreeToRadian(lat2);

	var sin = Math.sin;
	var cos = Math.cos;

	// var a = sin(lat / 2) * sin(lat / 2) + cos(degreeToRadian(lat1)) * cos(degreeToRadian(lat2)) * sin(long / 2) * sin(long / 2);
	// var c = 2 * Math.atan(Math.sqrt(a), Math.sqrt(1 - a));
	// var d = radius * c; 	// Distance in miles
	// return d;

	var dist = radius * Math.acos(sin(lat1rad) * sin(lat2rad) + cos(lat1rad) * cos(lat2rad) * cos(long));

	return dist;
}

function degreeToRadian(deg) {
	return deg * (Math.PI / 180);
}

app.use('/approve', (req, res) => {
	res.type('html').status(200);
	res.write("Approve endpoint to be triggered");
	res.end();
});

app.use('/delete', (req, res) => {
	var filter = { 'name': req.query.name };

	Resource.findOneAndDelete(filter, (err, orig) => {
		if (err) {
			res.json({ 'status': err });
		}
		else if (!orig) {
			res.json({ 'status': 'no resource' });
		}
		else {
			res.json({ 'status': 'success' });
		}
	});
});
/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('/public/new_resource.html'); });

app.listen(3000, () => {
	console.log('Listening on port 3000');
});
