/**
 * Title: Plugin For PrimeFaces Month/Year Picker
 * Description: Plugin to extend the PrimeFaces datepicker (based on jQuery UI datepicker) widget that allows the used to choose only a month and year from an interactive calendar UI.
 */
PrimeFaces.widget.MonthYearPicker = PrimeFaces.widget.BaseWidget.extend({
    init: function(config) {
        this._super(config);
        this.input = $(this.jqId + "_input");
        this.jqEl = this.input;
        var self = this;
        this.configureLocale();
        this.bindDateSelectListener();
        this.bindViewChangeListener();
        this.bindClickListener();
        if (this.cfg.popup) {
            PrimeFaces.skinInput(this.jqEl);
            if (this.cfg.behaviors) {
                PrimeFaces.attachBehaviors(this.jqEl, this.cfg.behaviors);
            }

            this.bindBeforeShowListener();
            this.bindOnCloseListener();

            this.jq.data("primefaces-overlay-target", this.id).find("*").data("primefaces-overlay-target", this.id);
        }

        if (!this.cfg.disabled) {
            this.jqEl.datepicker(this.cfg);
        }
        this.input.data(PrimeFaces.CLIENT_ID_DATA, this.id);
    },
    refresh: function(config) {
        if (config.popup && $.datepicker._lastInput && (config.id + "_input") === $.datepicker._lastInput.id) {
            $.datepicker._hideDatepicker();
        }
        this.init(config);
    },
    configureLocale: function() {
        var a = PrimeFaces.locales[this.cfg.locale];
        if (a) {
            for (var b in a) {
                this.cfg[b] = a[b];
            }
        }
    },
    bindClickListener: function() {
    	var self = this;
    	this.input.on("click", function() {
    		self.hideCalendar();
    		self.fireClickEvent();
        });
	},
	fireClickEvent: function() {
		var self = this;
		self.setDate(self.cfg.selectedDate ? self.cfg.selectedDate : new Date());
	},
    bindBeforeShowListener: function () {
		var self = this;
		this.cfg.beforeShow = function(element, instance) {
			self.fireBeforeShowEvent(element, instance);
        }
	},
	fireBeforeShowEvent: function(element, instance) {
		var self = this;
    	$("#ui-datepicker-div").addClass("ui-monthYearPicker-div");
        setTimeout(function() {
            $("#ui-datepicker-div").css("z-index", ++PrimeFaces.zindex);
        }, 1);
        if (PrimeFaces.env.touch && !self.input.attr("readonly") && self.cfg.showOn && self.cfg.showOn === "button") {
            $(this).prop("readonly", true);
        }
        if (self.cfg.preShow) {
            return self.cfg.preShow.call(self, element, instance);
        }
	},
	bindOnCloseListener: function() {
		var self = this;
		this.cfg.onClose = function(element, instance) {
        	self.fireOnCloseEvent();
        	self.fireDateSelectEvent();
        }
	},
	fireOnCloseEvent: function(element, instance) {
		var self = this;
		if (self.cfg.popup && self.cfg.selectedDate) {
	        self.input.val($.datepicker.formatDate(self.cfg.dateFormat, self.cfg.selectedDate));
    	}
		$("#ui-datepicker-div").removeClass("ui-monthYearPicker-div");
    	if (PrimeFaces.env.touch && !self.input.attr("readonly") && self.cfg.showOn && self.cfg.showOn === "button") {
    		$(this).attr("readonly", false);
        }
	},
    bindDateSelectListener: function() {
        var self = this;
        self.cfg.onSelect = function(textDate, instance) {
            if (self.cfg.popup) {
            	self.fireDateSelectEvent()
            } else {
                var date = $.datepicker.formatDate(self.cfg.dateFormat, self.getDate());
                self.input.val(date);
                self.fireDateSelectEvent();
            }
        }
    },
    fireDateSelectEvent: function() {
    	var self = this;
        if (self.cfg.behaviors) {
            var fun = self.cfg.behaviors.dateSelect;
            if (fun) {
            	fun.call(self);
            }
        }
    },
    bindViewChangeListener: function() {
        var self = this;
        this.cfg.onChangeMonthYear = function(year, month, instance) {
        	self.hideCalendar();
        	self.fireViewChangeEvent(year, month);
        }
    },
    fireViewChangeEvent: function(year, month) {
    	var self = this;
    	if (self.cfg.popup) {
    		var date = new Date(year, month - 1);
    		self.cfg.selectedDate = date;
    	}
        if (self.cfg.behaviors) {
            var fun = self.cfg.behaviors.viewChange;
            if (fun) {
                var result = {
                    params: [{
                        name: self.id + "_month",
                        value: month
                    }, {
                        name: self.id + "_year",
                        value: year
                    }]
                };
                fun.call(self, result);
            }
        }
    },
    hideCalendar: function() {
    	setTimeout(function() {
    		$('.ui-datepicker-calendar').css('display','none');
    	}, 1);
    },
    setDate: function(date) {
        this.jqEl.datepicker("setDate", date);
    },
    getDate: function() {
        return this.jqEl.datepicker("getDate");
    },
    enable: function() {
        this.jqEl.datepicker("enable");
    },
    disable: function() {
        this.jqEl.datepicker("disable");
    },
    hasBehavior: function(name) {
        if (this.cfg.behaviors) {
            return this.cfg.behaviors[name] !== undefined;
        }
        return false;
    }
});
