angular
    .module('app')
    .config(routesConfig);

/** @ngInject */
function routesConfig($stateProvider, $urlRouterProvider, $locationProvider) {
    $locationProvider.html5Mode(true).hashPrefix('!');
    $urlRouterProvider.otherwise('/');



    $stateProvider
        .state('login', {
            url: '/',
            component: 'login'
        });


    $stateProvider
        .state('home', {
            url: '/home',
            component: 'home'
        });


    $stateProvider
        .state('register', {
            url: '/register',
            component: 'register'
        });

    $stateProvider
        .state('chat', {
            url: '/chat',
            component: 'chat'
        });
}
