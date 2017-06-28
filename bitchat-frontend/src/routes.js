angular
    .module('app')
    .config(routesConfig);

/** @ngInject */
function routesConfig($stateProvider, $urlRouterProvider, $locationProvider) {
    $locationProvider.html5Mode(true).hashPrefix('!');
    $urlRouterProvider.otherwise('/');



    $stateProvider
        .state('app', {
            url: '/',
            component: 'app'
        });


    $stateProvider
        .state('home', {
            url: '/home',
            component: 'home'
        });


    $stateProvider
        .state('registrar', {
            url: '/registrar',
            component: 'registrar'
        });

    $stateProvider
        .state('chat', {
            url: '/chat',
            component: 'chat'
        });
}
