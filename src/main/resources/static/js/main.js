// Event Management System
class EventManager {
    constructor() {
        this.events = [];
        this.currentEvent = null;
        this.init();
    }

    async init() {
        await this.loadEvents();
        this.setupEventListeners();
        this.renderEvents();
    }

    async loadEvents() {
        try {
            const response = await fetch('/api/events');
            this.events = await response.json();
        } catch (error) {
            console.error('Error loading events:', error);
            this.showAlert('Error loading events', 'danger');
        }
    }

    setupEventListeners() {
        // Add Event Form
        const addEventForm = document.getElementById('addEventForm');
        if (addEventForm) {
            addEventForm.addEventListener('submit', (e) => this.handleAddEvent(e));
        }

        // Search Bar
        const searchInput = document.getElementById('searchInput');
        if (searchInput) {
            searchInput.addEventListener('input', (e) => this.handleSearch(e));
        }
    }

    async handleAddEvent(e) {
        e.preventDefault();
        const formData = new FormData(e.target);
        const eventData = {
            name: formData.get('name'),
            description: formData.get('description'),
            startDateTime: formData.get('startDateTime'),
            endDateTime: formData.get('endDateTime'),
            venue: formData.get('venue'),
            price: parseFloat(formData.get('price')),

            imageUrl: formData.get('imageUrl')
        };

        try {
            const response = await fetch('/api/events', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(eventData)
            });

            if (response.ok) {
                await this.loadEvents();
                this.renderEvents();
                e.target.reset();
                this.showAlert('Event added successfully!', 'success');
            } else {
                throw new Error('Failed to add event');
            }
        } catch (error) {
            console.error('Error adding event:', error);
            this.showAlert('Error adding event', 'danger');
        }
    }

    async handleUpdateEvent(id, eventData) {
        try {
            const response = await fetch(`/api/events/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(eventData)
            });

            if (response.ok) {
                await this.loadEvents();
                this.renderEvents();
                this.showAlert('Event updated successfully!', 'success');
            } else {
                throw new Error('Failed to update event');
            }
        } catch (error) {
            console.error('Error updating event:', error);
            this.showAlert('Error updating event', 'danger');
        }
    }

    async handleDeleteEvent(id) {
        if (confirm('Are you sure you want to delete this event?')) {
            try {
                const response = await fetch(`/api/events/${id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    await this.loadEvents();
                    this.renderEvents();
                    this.showAlert('Event deleted successfully!', 'success');
                } else {
                    throw new Error('Failed to delete event');
                }
            } catch (error) {
                console.error('Error deleting event:', error);
                this.showAlert('Error deleting event', 'danger');
            }
        }
    }

    handleSearch(e) {
        const searchTerm = e.target.value.toLowerCase();
        const filteredEvents = this.events.filter(event =>
            event.name.toLowerCase().includes(searchTerm) ||
            event.description.toLowerCase().includes(searchTerm) ||
            event.venue.toLowerCase().includes(searchTerm)
        );
        this.renderEvents(filteredEvents);
    }

    renderEvents(eventsToRender = this.events) {
        const eventList = document.getElementById('eventList');
        if (!eventList) return;

        eventList.innerHTML = eventsToRender.map(event => `
            <div class="col-md-4">
                <div class="card event-card">
                    <img src="${event.imageUrl || 'https://via.placeholder.com/300x200'}"
                         class="card-img-top event-image"
                         alt="${event.name}">
                    <div class="card-body event-details">
                        <h5 class="card-title">${event.name}</h5>
                        <p class="event-venue">
                            <i class="fas fa-map-marker-alt"></i> ${event.venue}
                        </p>
                        <p class="event-datetime">
                            <i class="fas fa-calendar"></i> ${new Date(event.startDateTime).toLocaleString()}
                        </p>
                        <p class="event-description">${event.description}</p>

                        <p class="event-price">$${event.price}</p>
                        <div class="btn-group">
                            <button class="btn btn-primary btn-action"
                                    onclick="eventManager.handleEditEvent(${event.id})">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <button class="btn btn-danger btn-action"
                                    onclick="eventManager.handleDeleteEvent(${event.id})">
                                <i class="fas fa-trash"></i> Delete
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `).join('');
    }

    showAlert(message, type) {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        document.body.insertBefore(alertDiv, document.body.firstChild);
        setTimeout(() => alertDiv.remove(), 5000);
    }
}

// Initialize the Event Manager
const eventManager = new EventManager();