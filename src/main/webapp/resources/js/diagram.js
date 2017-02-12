// Drawing histogram 	
//  Using named IIFE so that scope of defined keeps confined into function / defined vars aren't global 
// 
(function drawDiagram () {
	
	var jsonData = getJsonData();
	var jsonDataset= jsonData.jsonSamples;
	var tempMin= jsonData.tempMin;
	var tempMax= jsonData.tempMax;
	
	var pointNumber = jsonDataset.length;
	var margin = {top: 20, right: 20, bottom: 30, left: 50};
//	var barPadding = 1;
	var w = 10 * pointNumber;  // look nice when w=700px for 92 point 
	var h = 200;

	// dimension of whle graph (=histogram + axes)
	widthGraph = w + margin.left + margin.right,
	heightGraph = h + margin.top + margin.bottom;

	var xScale = d3.scaleBand()
    	.domain(d3.range(jsonDataset.length)) // -> x [0..datase.length-1]
    	//.domain(d3.extent(jsonDataset, function(d) { return d.timestamp; }))
    	.range([0, w]);
	
	var yScale = d3.scaleLinear()
				//  //.domain([2, d3.max(jsonDataset, function(d) { return d.temp; })]) //d3.max(dataset)])
		// d.temp gets mapped into the y-coordinate in renage() 0->h, max(temp)->0 (domain=data scope)
		.domain([0, d3.max(jsonDataset, function(d) {return d.temp;})]) 
		.range([h,0]); // range of y coordinates higher temp. values at the top (closer to origin) 

	// Create SVG element
	var svg = d3.select("div#diagram")
		.append("svg")
		.attr("width", widthGraph)
		.attr("height", heightGraph)
		.append("g")
			.attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
	svg.selectAll("rect")
		.data(jsonDataset)
		.enter()
		.append("rect")
		.attr("x", function(d, i) {
			return xScale(i);
		})
		.attr("y", function(d) {
			return yScale(d.temp)
		})
		.attr("width", xScale.bandwidth()) // rangeBand() in v3.3
		.attr("height", function(d) {
	   		return h - yScale(d.temp);
		})
		.attr("class", function(d) {
			return getClassForSample(d.eval);
		})
		.append("title")
		.text(function(d) {
			return getTooltipForSample(d);
		});
			
	  // Add the X Axis
	  svg.append("g")
	  	  .attr("class", "axis axis-x")
	      .attr("transform", "translate(0," + h + ")")
	      .call(d3.axisBottom(xScale))
	      .selectAll("text")
	      	.style("text-anchor","end")
	      	.attr("dx", "-0.8em")
	      	.attr("dy", "-0.5em") // adjust rotated x label towards left
	      	.attr("transform", "rotate(-90)");
	      

	  // Add the Y Axis
	  svg.append("g")
	      .attr("class", "axis axis-y")
	      .call(d3.axisLeft(yScale));
	  
	  var thresholdTemps = [
	     {temp: tempMin, cssClass: "min-threshold"},
		 {temp: tempMax, cssClass: "max-threshold"}
	  ];
	  
	  thresholdTemps.forEach( function(elem) {
		  svg.append("g")
		  	.attr("class", elem.cssClass)
		  	.append("line")
		  		.attr("class", elem.cssClass)
//	    		.style("stroke","black")
	    		.attr("x1",0)
	    		.attr("y1",yScale(elem.temp))
	    		.attr("x2",w)
	    		.attr("y2",yScale(elem.temp))
	    		; //.style("stroke-dasharray", ("2, 2"));
	  });
})();

function getClassForSample(eval) {
	return "temp-" + eval;
}

function getTooltipForSample(d) {
	return d.eval + "\nDate: " + d.timestamp + "\nTemperature: " + d.temp;
}
/*<![CDATA[*/ 
			// TODO remove
// This doesn't work as this is a static version param buildVersion cannot be resolved
//		    var message = [["${buildVersion}"]];
// 		    console.log(message);

/*]]>*/
		   