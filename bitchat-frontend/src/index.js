angular
    .module('app', ['ui.router', 'ui-notification', 'ngCookies'])
    .constant('BackendUrl', 'http://localhost:8080/bitchat')
    .constant('WebsocketUrl', 'ws://localhost:8080/bitchat')
    .config(function(NotificationProvider) {
        NotificationProvider.setOptions({
            delay: 10000,
            startTop: 20,
            startRight: 10,
            verticalSpacing: 20,
            horizontalSpacing: 20
        });
    })
    .run(runBlock);

/** @ngInject */
function runBlock($rootScope, $state, $cookies) {

    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){
        console.log($cookies.getObject('user'));

        if (angular.isUndefined($rootScope.user)){
            $state.go('login');
        }
    });


}


