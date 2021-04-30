// set up Express
var express = require('express');
var app = express();

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
// import the Resource class from Resource.js
var Resource = require('./Resource.js');
var Suggestion = require('./Suggestion.js');

/***************************************/

// endpoint for creating a new Resource
// this is the action of the "create new Resource" form


app.use('/create', (req, res) => {
	// construct the Resource from the form data which is in the request body
	var newResource = new Resource({
		name: req.body.name,
		website: req.body.website,
		phone: req.body.phone,
		description: req.body.description,
	});

	// save the Resource to the database
	newResource.save((err) => {
		if (err) {
			res.type('html').status(200);
			res.write('uh oh: ' + err);
			console.log(err);
			res.end();
		}
		else {
			// display the "successfully created" message
			res.send('Successfully added ' + newResource.name + ' to the database');
		}
	});
}
);

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
				res.write('Here are all the resources in the database:');
				res.write('<ol>');
				// show all the resources
				suggestList.forEach((suggestion) => {
					res.write('<li>' + suggestion.name);
					res.write('<ul>');
					res.write('<li>Description: ' + suggestion.description + '</li>'
						+ '<li>Phone Number: ' + suggestion.phone + '</li>'
						+ '<li>Website: ' + suggestion.website + '</li>'
						+ '<br>');
					res.write('</ul>');
				});
				res.write('</ol>');
				res.end();
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
				"phone": resource.phone, "description": resource.description
			});
		}
		else {
			// construct an array out of the result
			var returnArray = [];
			resources.forEach((resource) => {
				returnArray.push({
					"name": resource.name, "website": resource.website,
					"phone": resource.phone, "description": resource.description
				});
			});
			// send it back as JSON Array
			res.json(returnArray);
		}

	});
});

app.use('/update', (req,res) => {
	var filter = {'name': req.query.name};
	var action = null;

	if(req.query.website != null && req.query.phone != null && req.query.description){
		action = {'$set' : {'website': req.query.website, 'phone': req.query.phone, 'description': req.query.description}};
	}else if(req.query.website != null && req.query.phone != null){
		action = {'$set' : {'website' : req.query.website, 'phone': req.query.phone}};
	}else if(req.query.website != null && req.query.description != null){
		action = {'$set' : {'website' : req.query.website, 'description': req.query.description}};
	}else if(req.query.phone != null && req.query.description != null){
		action = {'$set' : {'phone' : req.query.phone, 'description': req.query.description}};
	}else if(req.query.website != null){
		action = {'$set' : {'website' : req.query.website}};
	}else if(req.query.phone != null){
		action = {'$set' : {'phone' : req.query.phone}};
	}else if(req.query.description != null){
		action = {'$set' : {'description' : req.query.description}};
	}

	if(action != null){
		Resource.findOneAndUpdate(filter,action,(err,orig) => {
			if(err){
				res.json({'status':err});
			}
			else if (!orig){
				res.json({'status': 'no resource'});
			}
			else{
				res.json({'status': 'success'});
			}
		});
	}else{
		res.json({'status' : 'no updates performed'});
	}
});




/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('/public/new_resource.html'); });

app.listen(3000, () => {
	console.log('Listening on port 3000');
});
