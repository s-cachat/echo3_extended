/* global Echo, Core, moment */
DateSelect2 = {};
DateSelect2 = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("DateSelect2", this);
    },
    componentType: "DateSelect2"
});
DateSelect2.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("DateSelect2", this);
        componentType : "DateSelect2";
       document.head.innerHTML = document.head.innerHTML +'<link rel="stylesheet" type="text/css" href="dateSelect2/datetimerange.css" />';
    },
    _picker: null,
    renderAdd: function (update, parentElement) {
        const options = {
            parentEl: parentElement
        };

        var val = this.component.get('locale');
        if (val !== undefined) {
            const json = JSON.parse(val);

            if (json.lang !== undefined) {
                moment.locale(json.lang);
            }

            options.locale = {};
            if (json.format !== undefined) {
                options.locale.format = json.format;
            }

            if (json.applyLabel !== undefined) {
                options.locale.applyLabel = json.applyLabel;
            }

            if (json.cancelLabel !== undefined) {
                options.locale.cancelLabel = json.cancelLabel;
            }

            if (json.clearLabel !== undefined) {
                options.locale.clearLabel = json.clearLabel;
            }
        }

        val = this.component.get('startDate');
        if (!val) {
            options.startDate = null;
        } else {
            options.startDate = new Date(val);
        }

        val = this.component.get('endDate');
        if (!val) {
            options.endDate = null;
        } else {
            options.endDate = new Date(val);
        }

        val = this.component.get('minDate');
        if (!val) {
            options.minDate = false;
        } else {
            options.minDate = new Date(val);
        }

        val = this.component.get('maxDate');
        if (!val) {
            options.maxDate = false;
        } else {
            options.maxDate = new Date(val);
        }

        val = this.component.get('minYear');
        if (val !== undefined) {
            options.minYear = val;
        }

        val = this.component.get('maxYear');
        if (val !== undefined) {
            options.maxYear = val;
        }

        val = this.component.get('dateLimit');
        if (val !== undefined) {
            options.dateLimit = val;
        }

        val = this.component.get('autoApply');
        if (val !== undefined) {
            options.autoApply = val;
        }

        val = this.component.get('singleDatePicker');
        if (val !== undefined) {
            options.singleDatePicker = val;
        }

        val = this.component.get('showDropdowns');
        if (val !== undefined) {
            options.showDropdowns = val;
        }

        val = this.component.get('showWeekNumbers');
        if (val !== undefined) {
            options.showWeekNumbers = val;
        }

        val = this.component.get('showISOWeekNumbers');
        if (val !== undefined) {
            options.showISOWeekNumbers = val;
        }

        val = this.component.get('showCustomRangeLabel');
        if (val !== undefined) {
            options.showCustomRangeLabel = val;
        }

        val = this.component.get('timePicker');
        if (val !== undefined) {
            options.timePicker = val;
        }

        val = this.component.get('timePicker24Hour');
        if (val !== undefined) {
            options.timePicker24Hour = val;
        }

        val = this.component.get('timePickerIncrement');
        if (val !== undefined) {
            options.timePickerIncrement = val;
        }

        val = this.component.get('timePickerSeconds');
        if (val !== undefined) {
            options.timePickerSeconds = val;
        }

        val = this.component.get('linkedCalendars');
        if (val !== undefined) {
            options.linkedCalendars = val;
        }

        val = this.component.get('autoUpdateInput');
        if (val !== undefined) {
            options.autoUpdateInput = val;
        }

        val = this.component.get('alwaysShowCalendars');
        if (val !== undefined) {
            options.alwaysShowCalendars = val;
        }

        val = this.component.get('ranges');
        if (val !== undefined) {
            options.ranges = val;
        }

        val = this.component.get('opens');
        if (val !== undefined) {
            options.opens = val;
        }

        val = this.component.get('drops');
        if (val !== undefined) {
            options.drops = val;
        }

        val = this.component.get('buttonClasses');
        if (val !== undefined) {
            options.buttonClasses = val;
        }

        val = this.component.get('applyButtonClasses');
        if (val !== undefined) {
            options.applyButtonClasses = val;
        }

        val = this.component.get('cancelButtonClasses');
        if (val !== undefined) {
            options.cancelButtonClasses = val;
        }

        const element = document.createElement("input");
        element.id = this.component.renderId.replaceAll('.', '_');
        element.type = "text";
        this._picker = new DateRangePicker(element, options);

        const _this = this;
        window.addEventListener('apply.daterangepicker', function (ev) {
            if (ev.detail !== _this._picker) {
                return;
            }
            _this.component.fireEvent({type: 'apply', source: _this.component, v: !ev.detail.startDate || !ev.detail.startDate.isValid() ? 'null' : ev.detail.startDate.format()});
        });
        this.component.addListener('apply', function (e) {
            e.source.peer.component.set('startDate', e.v);
        });

        window.addEventListener('clear.daterangepicker', function (ev) {
            if (ev.detail !== _this._picker) {
                return;
            }
            _this.component.fireEvent({type: 'clear', source: _this.component});
        });
        this.component.addListener('clear', function (e) {
            e.source.peer.component.set('startDate', 'null');
        });

        window.addEventListener('show.daterangepicker', function (ev) {
            if (ev.detail !== _this._picker) {
                return;
            }
            _this.component.fireEvent({type: 'show', source: _this.component});
        });
        this.component.addListener('show', function (e) {
        });

        window.addEventListener('hide.daterangepicker', function (ev) {
            if (ev.detail !== _this._picker) {
                return;
            }
            _this.component.fireEvent({type: 'hide', source: _this.component});
        });
        this.component.addListener('hide', function (e) {
        });

        window.addEventListener('showCalendar.daterangepicker', function (ev) {
            if (ev.detail !== _this._picker) {
                return;
            }
            _this.component.fireEvent({type: 'showCalendar', source: _this.component});
        });
        this.component.addListener('showCalendar', function (e) {
        });

        window.addEventListener('hideCalendar.daterangepicker', function (ev) {
            if (ev.detail !== _this._picker) {
                return;
            }
            _this.component.fireEvent({type: 'hideCalendar', source: _this.component});
        });
        this.component.addListener('hideCalendar', function (e) {
        });

        window.addEventListener('cancel.daterangepicker', function (ev) {
            if (ev.detail !== _this._picker) {
                return;
            }
            _this.component.fireEvent({type: 'cancel', source: _this.component});
        });
        this.component.addListener('cancel', function (e) {
        });

        Echo.Sync.renderComponentDefaults(this.component, this._picker.element);
        parentElement.appendChild(this._picker.element);
    },
    renderDisplay: function () {
        if (!this._picker) {
        }
    },
    renderDispose: function (update) {
        this._picker = null;
    },
    renderUpdate: function (update) {
        if (update.hasUpdatedProperties()) {
            const properties = update.getUpdatedPropertyNames();
            for (var property of properties) {
                var val = this.component.get(property);
                switch (property) {
                    case 'locale':
                    {
                        if (val !== undefined) {
                            const json = JSON.parse(val);

                            if (json.lang !== undefined) {
                                moment.locale(json.lang);
                            }

                            var locale = {};
                            if (json.format !== undefined) {
                                locale.format = json.format;
                            }

                            if (json.applyLabel !== undefined) {
                                locale.applyLabel = json.applyLabel;
                            }

                            if (json.cancelLabel !== undefined) {
                                locale.cancelLabel = json.cancelLabel;
                            }

                            if (json.clearLabel !== undefined) {
                                locale.clearLabel = json.clearLabel;
                            }

                            this._picker.locale.assign(locale);
                            this._picker.updateView();
                        }
                        break;
                    }

                    case 'startDate':
                    {
                        this._picker.setStartDate(!val || val == 'null' ? null : new Date(val));
                        break;
                    }

                    case 'endDate':
                    {
                        this._picker.setEndDate(!val || val == 'null' ? null : new Date(val));
                        break;
                    }

                    case 'minDate':
                    case 'maxDate':
                    {
                        if (!val || val == 'null') {
                            this._picker[property] = false;
                        } else {
                            this._picker[property] = moment(val);
                        }
                        break;
                    }

                    default:
                        this._picker[property] = this.component.get(property);
                        break;
                }
            }
        }
        return true;
    }
});
