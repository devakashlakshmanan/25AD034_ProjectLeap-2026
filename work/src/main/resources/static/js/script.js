/* =========================================================
   API CONFIGURATION
   ========================================================= */

const API_BASE_URL = "http://localhost:8080/api";


/* =========================================================
   PAGE INITIALIZATION
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    loadUsers();
    loadRideOffers();
    loadRideRequests();

    document
        .getElementById("userForm")
        .addEventListener("submit", createUser);

    document
        .getElementById("rideOfferForm")
        .addEventListener("submit", createRideOffer);

    document
        .getElementById("rideRequestForm")
        .addEventListener("submit", createRideRequest);
});


/* =========================================================
   NAVIGATION
   ========================================================= */

function showSection(sectionId) {

    const sections = document.querySelectorAll(".section");

    sections.forEach(section => {
        section.classList.remove("active-section");
    });

    const selectedSection = document.getElementById(sectionId);

    if (selectedSection) {
        selectedSection.classList.add("active-section");
    }

    const navButtons = document.querySelectorAll(".nav-btn");

    navButtons.forEach(button => {
        button.classList.remove("active");
    });

    navButtons.forEach(button => {

        const buttonText =
            button.textContent.trim().toLowerCase();

        if (
            (sectionId === "dashboard" &&
                buttonText === "dashboard") ||

            (sectionId === "users" &&
                buttonText === "users") ||

            (sectionId === "rides" &&
                buttonText === "ride offers") ||

            (sectionId === "requests" &&
                buttonText === "ride requests")
        ) {
            button.classList.add("active");
        }
    });
}


/* =========================================================
   NOTIFICATION
   ========================================================= */

function showNotification(message, type = "success") {

    const notification =
        document.getElementById("notification");

    notification.textContent = message;

    notification.className =
        "notification show " + type;

    setTimeout(() => {
        notification.className = "notification";
    }, 3000);
}


/* =========================================================
   ERROR HANDLING
   ========================================================= */

async function getErrorMessage(response) {

    try {

        const data = await response.json();

        if (data.message) {
            return data.message;
        }

        if (data.error) {
            return data.error;
        }

        return "Request failed";

    } catch (error) {

        return "Request failed with status " +
            response.status;
    }
}


/* =========================================================
   USER MODULE
   ========================================================= */

/*
    POST /api/users
*/

async function createUser(event) {

    event.preventDefault();

    const userData = {

        name:
            document.getElementById("userName").value.trim(),

        email:
            document.getElementById("userEmail").value.trim(),

        phone:
            document.getElementById("userPhone").value.trim(),

        userType:
        document.getElementById("userType").value,

        origin:
            document.getElementById("userOrigin").value.trim(),

        destination:
            document.getElementById("userDestination").value.trim(),

        route:
            document.getElementById("userRoute").value.trim(),

        preferredTime:
            document.getElementById("preferredTime").value.trim()
    };


    try {

        const response = await fetch(
            `${API_BASE_URL}/users`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(userData)
            }
        );


        if (!response.ok) {

            const message =
                await getErrorMessage(response);

            throw new Error(message);
        }


        const createdUser =
            await response.json();


        showNotification(
            `User created successfully. ID: ${createdUser.userId}`
        );


        document
            .getElementById("userForm")
            .reset();


        await loadUsers();

    } catch (error) {

        console.error(error);

        showNotification(
            error.message,
            "error"
        );
    }
}


/*
    GET /api/users
*/

async function loadUsers() {

    try {

        const response = await fetch(
            `${API_BASE_URL}/users`
        );


        if (!response.ok) {

            const message =
                await getErrorMessage(response);

            throw new Error(message);
        }


        const users =
            await response.json();


        displayUsers(users);

        updateDashboardUsers(users.length);

        populateUserDropdowns(users);

    } catch (error) {

        console.error(error);

        document.getElementById("usersList").innerHTML =
            `<p class="empty-message">
                Unable to load users.
            </p>`;

        showNotification(
            "Unable to load users",
            "error"
        );
    }
}


/*
    Display users
*/

function displayUsers(users) {

    const usersList =
        document.getElementById("usersList");


    if (!users || users.length === 0) {

        usersList.innerHTML =
            `<p class="empty-message">
                No users found.
            </p>`;

        return;
    }


    usersList.innerHTML = users.map(user => {

        return `
            <div class="user-item">

                <div class="item-top">

                    <div>
                        <div class="item-title">
                            ${escapeHtml(user.name)}
                        </div>

                        <div class="item-subtitle">
                            ${escapeHtml(user.email)}
                        </div>
                    </div>

                    <span class="badge badge-active">
                        ${escapeHtml(user.userType)}
                    </span>

                </div>


                <div class="item-details">

                    <div class="detail">

                        <span class="detail-label">
                            User ID
                        </span>

                        <span class="detail-value">
                            ${user.userId}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Phone
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(user.phone)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Origin
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(user.origin)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Destination
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(user.destination)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Route
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(user.route)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Preferred Time
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(user.preferredTime)}
                        </span>

                    </div>

                </div>

            </div>
        `;

    }).join("");
}


/*
    Populate driver and rider dropdowns
*/

function populateUserDropdowns(users) {

    const driverDropdown =
        document.getElementById("driverId");

    const riderDropdown =
        document.getElementById("riderId");


    driverDropdown.innerHTML =
        `<option value="">
            Select driver
        </option>`;


    riderDropdown.innerHTML =
        `<option value="">
            Select rider
        </option>`;


    users.forEach(user => {

        const driverOption =
            document.createElement("option");

        driverOption.value =
            user.userId;

        driverOption.textContent =
            `${user.name} (ID: ${user.userId})`;


        driverDropdown.appendChild(
            driverOption
        );


        const riderOption =
            document.createElement("option");

        riderOption.value =
            user.userId;

        riderOption.textContent =
            `${user.name} (ID: ${user.userId})`;


        riderDropdown.appendChild(
            riderOption
        );
    });
}


/* =========================================================
   RIDE OFFER MODULE
   ========================================================= */

/*
    POST /api/ride-offers
*/

async function createRideOffer(event) {

    event.preventDefault();


    const rideData = {

        driverId:
            Number(
                document.getElementById("driverId").value
            ),

        origin:
            document.getElementById("rideOrigin")
                .value.trim(),

        destination:
            document.getElementById("rideDestination")
                .value.trim(),

        route:
            document.getElementById("rideRoute")
                .value.trim(),

        rideDate:
        document.getElementById("rideDate")
            .value,

        departureTime:
            document.getElementById("departureTime")
                .value.trim(),

        availableSeats:
            Number(
                document.getElementById("availableSeats")
                    .value
            ),

        status:
        document.getElementById("rideStatus")
            .value
    };


    try {

        const response = await fetch(
            `${API_BASE_URL}/ride-offers`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(rideData)
            }
        );


        if (!response.ok) {

            const message =
                await getErrorMessage(response);

            throw new Error(message);
        }


        const createdRide =
            await response.json();


        showNotification(
            `Ride offer created successfully. ID: ${createdRide.rideOfferId}`
        );


        document
            .getElementById("rideOfferForm")
            .reset();


        await loadRideOffers();

    } catch (error) {

        console.error(error);

        showNotification(
            error.message,
            "error"
        );
    }
}


/*
    GET /api/ride-offers
*/

async function loadRideOffers() {

    try {

        const response = await fetch(
            `${API_BASE_URL}/ride-offers`
        );


        if (!response.ok) {

            const message =
                await getErrorMessage(response);

            throw new Error(message);
        }


        const rides =
            await response.json();


        displayRideOffers(rides);

        updateDashboardRides(rides.length);

        populateRideDropdown(rides);

    } catch (error) {

        console.error(error);

        document.getElementById("ridesList").innerHTML =
            `<p class="empty-message">
                Unable to load ride offers.
            </p>`;

        showNotification(
            "Unable to load ride offers",
            "error"
        );
    }
}


/*
    Display ride offers
*/

function displayRideOffers(rides) {

    const ridesList =
        document.getElementById("ridesList");


    if (!rides || rides.length === 0) {

        ridesList.innerHTML =
            `<p class="empty-message">
                No ride offers found.
            </p>`;

        return;
    }


    ridesList.innerHTML = rides.map(ride => {

        const statusClass =
            getStatusClass(ride.status);


        return `
            <div class="ride-item">

                <div class="item-top">

                    <div>

                        <div class="item-title">
                            ${escapeHtml(ride.origin)}
                            →
                            ${escapeHtml(ride.destination)}
                        </div>

                        <div class="item-subtitle">
                            Driver:
                            ${escapeHtml(ride.driverName)}
                        </div>

                    </div>


                    <span class="badge ${statusClass}">
                        ${escapeHtml(ride.status)}
                    </span>

                </div>


                <div class="item-details">

                    <div class="detail">

                        <span class="detail-label">
                            Ride ID
                        </span>

                        <span class="detail-value">
                            ${ride.rideOfferId}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Driver ID
                        </span>

                        <span class="detail-value">
                            ${ride.driverId}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Route
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(ride.route)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Date
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(ride.rideDate)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Departure
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(ride.departureTime)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Available Seats
                        </span>

                        <span class="detail-value">
                            ${ride.availableSeats}
                        </span>

                    </div>

                </div>

            </div>
        `;

    }).join("");
}


/*
    Populate ride dropdown
*/

function populateRideDropdown(rides) {

    const dropdown =
        document.getElementById("rideOfferId");


    dropdown.innerHTML =
        `<option value="">
            Select ride
        </option>`;


    rides.forEach(ride => {

        const option =
            document.createElement("option");

        option.value =
            ride.rideOfferId;

        option.textContent =
            `#${ride.rideOfferId} - ` +
            `${ride.origin} → ${ride.destination} ` +
            `(${ride.availableSeats} seats)`;

        dropdown.appendChild(option);
    });
}


/* =========================================================
   RIDE REQUEST MODULE
   ========================================================= */

/*
    POST /api/ride-requests
*/

async function createRideRequest(event) {

    event.preventDefault();


    const riderId =
        Number(
            document.getElementById("riderId").value
        );


    const rideOfferId =
        Number(
            document.getElementById("rideOfferId").value
        );


    const requestData = {

        riderId: riderId,

        rideOfferId: rideOfferId,

        status: "Pending"
    };


    try {

        const response = await fetch(
            `${API_BASE_URL}/ride-requests`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(requestData)
            }
        );


        if (!response.ok) {

            const message =
                await getErrorMessage(response);

            throw new Error(message);
        }


        const createdRequest =
            await response.json();


        showNotification(
            `Ride request created successfully. ID: ${createdRequest.rideRequestId}`
        );


        document
            .getElementById("rideRequestForm")
            .reset();


        await loadRideRequests();

    } catch (error) {

        console.error(error);

        showNotification(
            error.message,
            "error"
        );
    }
}


/*
    GET /api/ride-requests
*/

async function loadRideRequests() {

    try {

        const response = await fetch(
            `${API_BASE_URL}/ride-requests`
        );


        if (!response.ok) {

            const message =
                await getErrorMessage(response);

            throw new Error(message);
        }


        const requests =
            await response.json();


        displayRideRequests(requests);

        updateDashboardRequests(requests.length);

    } catch (error) {

        console.error(error);

        document.getElementById("requestsList").innerHTML =
            `<p class="empty-message">
                Unable to load ride requests.
            </p>`;

        showNotification(
            "Unable to load ride requests",
            "error"
        );
    }
}


/*
    Display ride requests
*/

function displayRideRequests(requests) {

    const requestsList =
        document.getElementById("requestsList");


    if (!requests || requests.length === 0) {

        requestsList.innerHTML =
            `<p class="empty-message">
                No ride requests found.
            </p>`;

        return;
    }


    requestsList.innerHTML = requests.map(request => {

        const statusClass =
            getStatusClass(request.status);


        const actionButtons =
            request.status.toLowerCase() === "pending"
                ? `
                    <div class="request-actions">

                        <button
                            class="action-btn accept-btn"
                            onclick="updateRequestStatus(
                                ${request.rideRequestId},
                                'Accepted'
                            )">
                            Accept
                        </button>

                        <button
                            class="action-btn reject-btn"
                            onclick="updateRequestStatus(
                                ${request.rideRequestId},
                                'Rejected'
                            )">
                            Reject
                        </button>

                    </div>
                  `
                : "";


        return `
            <div class="request-item">

                <div class="item-top">

                    <div>

                        <div class="item-title">
                            ${escapeHtml(request.riderName)}
                            requested a ride
                        </div>

                        <div class="item-subtitle">
                            Ride Offer #${request.rideOfferId}
                        </div>

                    </div>


                    <span class="badge ${statusClass}">
                        ${escapeHtml(request.status)}
                    </span>

                </div>


                <div class="item-details">

                    <div class="detail">

                        <span class="detail-label">
                            Request ID
                        </span>

                        <span class="detail-value">
                            ${request.rideRequestId}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Rider
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(request.riderName)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Driver
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(request.driverName)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Route
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(request.origin)}
                            →
                            ${escapeHtml(request.destination)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Date
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(request.rideDate)}
                        </span>

                    </div>


                    <div class="detail">

                        <span class="detail-label">
                            Departure
                        </span>

                        <span class="detail-value">
                            ${escapeHtml(request.departureTime)}
                        </span>

                    </div>

                </div>

                ${actionButtons}

            </div>
        `;

    }).join("");
}


/*
    PUT /api/ride-requests/{id}/status
*/

async function updateRequestStatus(
    requestId,
    status
) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/ride-requests/${requestId}/status?status=${encodeURIComponent(status)}`,
            {
                method: "PUT"
            }
        );


        if (!response.ok) {

            const message =
                await getErrorMessage(response);

            throw new Error(message);
        }


        await response.json();


        showNotification(
            `Ride request ${status.toLowerCase()} successfully`
        );


        await loadRideRequests();

        /*
            If a request was accepted,
            available seats changed in the backend,
            so refresh the ride list too.
        */

        await loadRideOffers();

    } catch (error) {

        console.error(error);

        showNotification(
            error.message,
            "error"
        );
    }
}


/* =========================================================
   DASHBOARD COUNTERS
   ========================================================= */

function updateDashboardUsers(count) {

    document.getElementById(
        "dashboardUsers"
    ).textContent = count;
}


function updateDashboardRides(count) {

    document.getElementById(
        "dashboardRides"
    ).textContent = count;
}


function updateDashboardRequests(count) {

    document.getElementById(
        "dashboardRequests"
    ).textContent = count;
}


/* =========================================================
   STATUS CLASS
   ========================================================= */

function getStatusClass(status) {

    if (!status) {
        return "badge-pending";
    }


    switch (status.toLowerCase()) {

        case "active":
            return "badge-active";

        case "accepted":
            return "badge-accepted";

        case "pending":
            return "badge-pending";

        case "rejected":
            return "badge-rejected";

        case "inactive":
            return "badge-inactive";

        default:
            return "badge-pending";
    }
}


/* =========================================================
   HTML SAFETY
   ========================================================= */

function escapeHtml(value) {

    if (value === null || value === undefined) {
        return "";
    }


    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}