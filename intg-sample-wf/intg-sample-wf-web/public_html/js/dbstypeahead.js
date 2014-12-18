/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['ojs/ojcore', 'jquery', 'knockout','jqueryui'], 
       /*
        * @param {Object} oj 
        * @param {jQuery} $
        */
function(oj, $, ko)
{
    
(function ()
{ // make sure register is running
    
ko.bindingHandlers.dbsTypeAhead = {
    init: function(element, valueAccessor) {
        var _value = valueAccessor();
        $(element).dbsTypeAhead(_value);
                
    },
    update: function(element, valueAccessor) {
        
    }
};

$.widget( "dbs.dbsTypeAhead", {
	
	options: {
		delay: 500,
		minLength: 1,
		source: null,
                filterFunc: null,
                disabled: false,
                
		// event handlders
		response: null
	},

	requestIndex: 0,
	pending: 0,

	_create: function() {
		// Some browsers only repeat keydown events, not keypress events
		// The code for & in keypress is the same as the up arrow, 
		var suppressKeyPress, suppressKeyPressRepeat, suppressInput,
			nodeName = this.element[ 0 ].nodeName.toLowerCase(),
			isTextarea = nodeName === "textarea",
			isInput = nodeName === "input";

		this.isMultiLine =
			isTextarea ? true :
			isInput ? false :
			// All other element types are determined by whether or not they're contentEditable
			this.element.prop( "isContentEditable" );

		this.valueMethod = this.element[ isTextarea || isInput ? "val" : "text" ];
		
		this.element
			//.addClass( "ui-autocomplete-input" )
			.attr( "autocomplete", "off" );

		this._on( this.element, {
			keydown: function( event ) {
				if ( this.element.prop( "readOnly" ) ) {
					suppressKeyPress = true;
					suppressInput = true;
					suppressKeyPressRepeat = true;
					return;
				}

				suppressKeyPress = false;
				suppressInput = false;
				suppressKeyPressRepeat = false;
				var keyCode = $.ui.keyCode;
				switch ( event.keyCode ) {
				case keyCode.PAGE_UP:
					suppressKeyPress = true;
					this._keyEvent( "previousPage", event );
					break;
				case keyCode.PAGE_DOWN:
					suppressKeyPress = true;
					this._keyEvent( "nextPage", event );
					break;
				case keyCode.UP:
					suppressKeyPress = true;
					this._keyEvent( "previous", event );
					break;
				case keyCode.DOWN:
					suppressKeyPress = true;
					this._keyEvent( "next", event );
					break;
				case keyCode.ENTER:
					suppressKeyPressRepeat = true;
                                        suppressInput = true;
					this._searchTimeout( event );
                                        event.preventDefault();
					break;
				case keyCode.TAB:
					break;
				case keyCode.ESCAPE:
					break;
				default:
					suppressKeyPressRepeat = true;
                                        suppressInput = true;
					this._searchTimeout( event );
					break;
				}
			},
			keypress: function( event ) {
				if ( suppressKeyPress ) {
					suppressKeyPress = false;
					if ( !this.isMultiLine ) {
						event.preventDefault();
					}
					return;
				}
				if ( suppressKeyPressRepeat ) {
					return;
				}

				// replicate some key handlers to allow them to repeat in Firefox and Opera
				var keyCode = $.ui.keyCode;
				switch ( event.keyCode ) {
				case keyCode.PAGE_UP:
					this._keyEvent( "previousPage", event );
					break;
				case keyCode.PAGE_DOWN:
					this._keyEvent( "nextPage", event );
					break;
				case keyCode.UP:
					this._keyEvent( "previous", event );
					break;
				case keyCode.DOWN:
					this._keyEvent( "next", event );
					break;
				}
			},
			input: function( event ) {
				if ( suppressInput ) {
					suppressInput = false;
					event.preventDefault();
					return;
				}
				this._searchTimeout( event );
			},
			focus: function() {
				
			},
			blur: function( event ) {
                            
			}
		});

		this._initSource();

		// turning off autocomplete prevents the browser from remembering the
		// value when navigating through history
		this._on( this.window, {
			beforeunload: function() {
				this.element.removeAttr( "autocomplete" );
			}
		});
	},

	_setOption: function( key, value ) {
		this._super( key, value );
	},

	_initSource: function() {
		var array, url,
			that = this, filterFunc = that.options.filterFunc;
		if ( $.isArray( this.options.source ) ) {
			array = this.options.source;
			this.source = function( request, response ) {
				response( filterFunc( array, request.term ) );
			};
		} else if ( typeof this.options.source === "string" ) {
			url = this.options.source;
			this.source = function( request, response ) {
				if ( that.xhr ) {
					that.xhr.abort();
				}
				that.xhr = $.ajax({
					url: url,
					data: request,
					dataType: "json",
					success: function( data ) {
						response( data );
					},
					error: function() {
						response([]);
					}
				});
			};
		} else {
			this.source = this.options.source;
		}
	},

	_searchTimeout: function( event ) {
		clearTimeout( this.searching );
		this.searching = this._delay(function() {

			// Search if the value has changed, or if the user retypes the same value
			var equalValues = this.term === this._value(),
		            modifierKey = event.altKey || event.ctrlKey || event.metaKey || event.shiftKey;

			if ( !equalValues || ( equalValues && !modifierKey ) ) {
				//this.selectedItem = null;
				this.search( null, event );
			}
		}, this.options.delay );
	},

	search: function( value, event ) {
		value = value != null ? value : this._value();

		// always save the actual value, not the one passed as an argument
		this.term = this._value();
                
                var keyCode = $.ui.keyCode;
                if ( event.keyCode !== keyCode.BACKSPACE && 
                        event.keyCode !== keyCode.DELETE && 
                        event.keyCode !== keyCode.ENTER )
                {
                    if ( value.length < this.options.minLength ) {
			return this.close( event );
                    }
                }
		return this._search( value );
	},

	_search: function( value ) {
		this.pending++;
		//this.element.addClass( "ui-autocomplete-loading" );
		this.cancelSearch = false;

		this.source( { term: value }, this._response() );
	},

	_response: function() {
		var index = ++this.requestIndex;

		return $.proxy(function( content ) {
			if ( index === this.requestIndex ) {
				this.__response( content );
			}

			this.pending--;
			if ( !this.pending ) {
				//this.element.removeClass( "ui-autocomplete-loading" );
			}
		}, this );
	},

	__response: function( content ) {
		if ( !this.options.disabled && !this.cancelSearch ) {
			this._trigger( "response", null, { content: content } );
		} 
	},

	close: function( event ) {
		this.cancelSearch = true;
		//this._close( event );
	},

	_value: function() {
		return this.valueMethod.apply( this.element, arguments );
	},

	_keyEvent: function( keyEvent, event ) {
            if ( !this.isMultiLine ) {
		// prevents moving cursor to beginning/end of the text field in some browsers
	        event.preventDefault();
            }
	},
        
        destroy: function () {
            $.Widget.prototype.destroy.apply(this, arguments);
            this._destroy();
        },

	_destroy: function() {
		clearTimeout( this.searching );
		this.element
			//.removeClass( "ui-autocomplete-input" )
			.removeAttr( "autocomplete" );
	}
});



}()); // end make sure register is running

});



