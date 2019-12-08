// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy. 
// 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp 
//    do widelców. Wyniki przedstaw na wykresach.

const async = require('async');
const { PerformanceObserver, performance } = require('perf_hooks');

var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function(cb) { 
    // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
    //    i ponawia próbę, itd.
    var f = (delay) =>{
        setTimeout(() => {
            if(this.state === 1){
                f(2*delay);
            }else{
                this.state = 1;
                // console.log("fork acquired");
                if(cb) cb();
            }
        }, delay);
    }
    f(1);
}

Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    var times = [];

    var startAndEat = function(n){
        if(n === 0) {
            var sum = 0;
            for(var i = 0; i < times.length; i++){
                sum += times[i];
            }
            console.log(id+":"+sum/times.length);
            return;
        }
        start = performance.now();
        forks[f1].acquire(()=>{
            console.log(id+"acquired first");
            forks[f2].acquire(()=>{
                end = performance.now();
                times.push(end-start);
                console.log("eating " + id);
                setTimeout(() => {
                    forks[f2].release();
                    forks[f1].release();
                    console.log("Stopped eating "+ id);
                    rest(n);
                },  Math.floor((Math.random()*100)+50));
            })
        });

    }

    var rest = function(n){
        setTimeout(() => {
            // console.log("MLEKO"+n);
            startAndEat(n-1);            
        },  Math.floor((Math.random()*100)+50));
    }

    rest(count+1);
    // zaimplementuj rozwiązanie naiwne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
}

Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks,
        id = this.id;

    var f1,f2;
    var times = [];
    if(id % 2 === 0){
        f1 = this.f2;
        f2 = this.f1;
    }else{
        f1 = this.f1;
        f2 = this.f2;
    }
    var startAndEat = function(n){
        if(n === 0) {
            var sum = 0;
            for(var i = 0; i < times.length; i++){
                sum += times[i];
            }
            console.log(id+":"+sum/times.length);
            return;
        }
        start = performance.now();
        forks[f1].acquire(()=>{
            // console.log(id+"acquired first");
            forks[f2].acquire(()=>{
                end = performance.now();
                times.push(end-start);
                // console.log("eating " + id);
                setTimeout(() => {
                    forks[f2].release();
                    forks[f1].release();
                    // console.log("Stopped eating "+ id);
                    rest(n);
                },  Math.floor((Math.random()*100)+50));
            })
        });

    }

    var rest = function(n){
        setTimeout(() => {
            // console.log("MLEKO"+n);
            startAndEat(n-1);            
        },  Math.floor((Math.random()*100)+50));
    }

    rest(count+1);
    
    // zaimplementuj rozwiązanie asymetryczne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
}

var Jugde = function(n){
    this.state = n-1;
    return this;
}

Jugde.prototype.acquire = function(cb){
    var f = (delay) =>{
        setTimeout(() => {
            if(this.state <= 0){
                f(2*delay);
            }else{
                this.state -= 1;
                if(cb) cb();
            }
        }, delay);
    }
    f(1);
}

Jugde.prototype.release = function() { 
    this.state +=1; 
}

Philosopher.prototype.startConductor = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;
 
    var judge = new Jugde(forks.length);
    var start, end;
    var times = [];

    var startAndEat = function(n){
        if(n === 0) {
            var sum = 0;
            for(var i = 0; i < times.length; i++){
                sum += times[i];
            }
            console.log(id+":"+sum/times.length);
            return;
        }
        start = performance.now();
        judge.acquire(() =>{
            forks[f1].acquire(()=>{
                // console.log(id+"acquired first");
                forks[f2].acquire(()=>{
                    end = performance.now();
                    times.push(end-start);
                    // console.log("eating " + id);
                    setTimeout(() => {
                        forks[f2].release();
                        forks[f1].release();
                        judge.release();
                        // console.log("Stopped eating "+ id);
                        rest(n);
                    },  Math.floor((Math.random()*100)+50));
                })
            });
        });
    }
    var rest = function(n){
        setTimeout(() => {
            // console.log("MLEKO"+n);
            startAndEat(n-1);            
        },  Math.floor((Math.random()*100)+50));
    }

    rest(count+1);
    // zaimplementuj rozwiązanie z kelnerem
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
}


var N = 5;
var forks = [];
var philosophers = []
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

for (var i = 0; i < N; i++) {
    philosophers[i].startConductor(5);
}