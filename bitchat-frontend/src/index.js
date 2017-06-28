angular
    .module('app', ['ui.router', 'ui-notification'])
    .constant('BackendUrl', 'http://localhost:8080/bitchat')
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
function runBlock($rootScope, $state) {

    console.log($rootScope.user);

    if (angular.isUndefined($rootScope.user)){
        $state.go('login');
    }


}


