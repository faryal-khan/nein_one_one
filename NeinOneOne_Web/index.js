// set up Express
var express = require('express');
var app = express();

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

// import the Person class from Person.js
var Resource = require('./Resource.js');

/***************************************/

// endpoint for creating a new person
// this is the action of the "create new person" form
app.use('/create', (req, res) => {
    // construct the Person from the form data which is in the request body
    var newResource = new Resource ({
	name: req.body.name,
	website: req.body.website,
	phone: req.body.phone,
	description: req.body.description,
    });

    // save the person to the database
    newResource.save( (err) => { 
	if (err) {
	    res.type('html').status(200);
	    res.write('uh oh: ' + err);
	    console.log(err);
	    res.end();
	}
	else {
	    // display the "successfull created" message
	    res.send('successfully added ' + newResource.name + ' to the database');
	}
    } ); 
}
       );

// endpoint for showing all the people
app.use('/all', (req, res) => {
    
    // find all the Resource objects in the database
    Resource.find( {}, (err, resources) => {
	if (err) {
	    res.type('html').status(200);
	    console.log('uh oh' + err);
	    res.write(err);
	}
	else {
	    if (resources.length == 0) {
		res.type('html').status(200);
		res.write('There are no resources');
		res.end();
		return;
	    }
	    else {
		res.type('html').status(200);
		res.write('Here are the resources in the database:');
		res.write('<ul>');
		// show all the people
		resources.forEach( (resource) => {
		    res.write('<li>Name: ' + resource.name + '; website: ' + resource.website + '; phone: ' + resource.phone + '; description: ' + resource.description + '</li>');
		});
		res.write('</ul>');
		res.end();
	    }
	}
    }).sort({ 'name': 'asc' }); // this sorts them BEFORE rendering the results
});

// endpoint for accessing data via the web api
// to use this, make a request for /api to get an array of all Person objects
// or /api?name=[whatever] to get a single object
app.use('/api', (req, res) => {

    // construct the query object
    var queryObject = {};
    if (req.query.name) {
	// if there's a name in the query parameter, use it here
	queryObject = { "name" : req.query.name };
    }
    
    Resource.find( queryObject, (err, resources) => {
	console.log(resources);
	if (err) {
	    console.log('uh oh' + err);
	    res.json({});
	}
	else if (resources.length == 0) {
	    // no objects found, so send back empty json
	    res.json({});
	}
	else if (resources.length == 1 ) {
	    var resource = resources[0];
	    // send back a single JSON object
	    res.json( { "name" : resource.name , "website" : resource.age ,
			"phone" : resource.phone , "description" : resource.description}  );
	}
	else {
	    // construct an array out of the result
	    var returnArray = [];
	    resources.forEach( (resource) => {
		returnArray.push( { "name" : resource.name , "website" : resource.age ,
			"phone" : resource.phone , "description" : resource.description} );
	    });
	    // send it back as JSON Array
	    res.json(returnArray); 
	}
	
    });
});




/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('/public/resourceform.html'); } );

app.listen(3000,  () => {
    console.log('Listening on port 3000');
});
