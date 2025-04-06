

function setupEnvironment() {
	var env = karate.env; // get java system property 'karate.env'

	karate.log( 'karate.env system property was:', env );

	if ( !env ) {
		env = 'local'; // a custom 'intelligent' default
	}

	var config = { // base config JSON
		//appId: 'my.app.id',
		//appSecret: 'my.secret',
		host: 'localhost:8080',
		rootPath: '/api/v1/location',
		authRootPath: '/authenicate/token',
		baseUrl: 'http://localhost:8102/killroy/was/here'
	};

	const hosts = new Map([
		 [ 'local',    'localhost:8100' ]
		,[ 'dev',      'localhost:8102' ]
		,[ 'qa',       'qa:8102' ]
		,[ 'int',      'integration:8102' ]
		,[ 'preprod',  'preprod:8102' ]
		,[ 'demo',     'sales:8102' ]
		,[ 'prod',     'domain:8102' ]
	]);

	// activeHost = hosts.get( env );
	config.host = 'http://' + hosts.get( env )
	config.baseUrl = config.host + config.rootPath;

	config.authHost = 'https://' + hosts.get( env )
	config.authBaseUrl = config.authHost + config.authRootPath;

	// Allow use of karate.abort() to conditionally end a scenario early
	config.abortedStepsShouldPass = true;

	// don't waste time waiting for a connection or if servers don't respond within 5 seconds
	karate.configure('connectTimeout', 5000);
	karate.configure('readTimeout', 5000);
	karate.configure('retry', 1);
	karate.configure('report', { showLog: true, showAllSteps: true, logPrettyRequest: true, logPrettyResponse: true });

	karate.configure('ssl', true );

	karate.log( config );
	karate.log( 'baseURL: ', config.baseUrl );

	return config;
}