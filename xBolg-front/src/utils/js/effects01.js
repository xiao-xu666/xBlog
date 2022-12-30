window.onload = () => {
	let i = 0;
	let body = document.getElementsByClassName('effects01')[0];
	if (body != undefined) {
		body.onclick = function (e) {
			const a = ["富强", "民主", "文明", "和谐", "自由", "平等", "公正", "法治", "爱国", "敬业", "诚信", "友善"];
			var getRandomColor = function() {
				let color = ['#6D80FE','#23D2FD','#FBF40F','#FFA9AB','#09B0E8','#29F49A','#717CFE','#FC83EC','#535b9a','#30bcd7','#FF988B','#FF6B88']
				let random = Math.floor(Math.random() * color.length)
				console.log(random);
				return color[random];
			}
			let span = document.createElement("span");
			span.innerText = a[(i++) % a.length];
			let x = e.pageX;
			let y = e.pageY;
			
			span.style.cssText = "z-index:999999999999999999999999;top:" + (y - 20) + "px;left:" + x + "px;position:absolute;font-weight:bold;color:" + getRandomColor() + ";";
			body.appendChild(span);
			animate(span, {
				"top": y - 180,
				"opacity": 0
			},
				15, 0.01,
				function () {
					body.removeChild(span)
				})
		}
		function animate(obj, json, interval, sp, fn) {
			clearInterval(obj.timer);
			function getStyle(obj, arr) {
				if (obj.currentStyle) {
					return obj.currentStyle[arr]
				} else {
					return document.defaultView.getComputedStyle(obj, null)[arr].replace(/px/g, "")
				}
			}
			obj.timer = setInterval(function () {
				var flag = true;
				for (var arr in json) {
					var icur = 0;
					if (arr == "opacity") {
						icur = Math.round(parseFloat(getStyle(obj, arr)) * 100)
					} else {
						icur = parseInt(getStyle(obj, arr))
					}
					var speed = (json[arr] - icur) * sp;
					speed = speed > 0 ? Math.ceil(speed) : Math.floor(speed);
					if (icur != json[arr]) {
						flag = false
					}
					if (arr == "opacity") {
						obj.style.filter = "alpha(opacity : '+(icur + speed)+' )";
						obj.style.opacity = (icur + speed) / 100
					} else {
						obj.style[arr] = icur + speed + "px"
					}
				}
				if (flag) {
					clearInterval(obj.timer);
					if (fn) {
						fn()
					}
				}
			},
				interval)
		}
	}
}