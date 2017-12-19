const Task = require('../models/Task');

module.exports = {
	index: (req, res) => {
        Task.find({}).then(tasks => {
            let openTasks = [];
            let inProgressTasks = [];
            let finishedTasks = [];

            for(let i = 0; i < tasks.length; i++) {
                let currentTask = tasks[i];
                if(currentTask.status == 'Open') {
                    openTasks.push(currentTask);
                }
                else if(currentTask.status == 'In Progress') {
                    inProgressTasks.push(currentTask);
                }
                else if(currentTask.status == 'Finished') {
                    finishedTasks.push(currentTask);
                }
            }

            res.render('task/index', {openTasks: openTasks, inProgressTasks: inProgressTasks, finishedTasks: finishedTasks})
        });
	},
	createGet: (req, res) => {
        res.render('task/create');
	},
	createPost: (req, res) => {
        let task = req.body;
        Task.create(task).then(task => {
            res.redirect("/");
        }).catch(err => {
            task.error = 'Cannot create task.';
            res.render('task/create', task);
        });
	},
	editGet: (req, res) => {
        let taskId = req.params.id;
        Task.findById(taskId).then(task => {
            if (task) {
                res.render('task/edit', task );
            }
            else {
                res.redirect('/');
            }
        }).catch(err => res.redirect('/'));
    },
	editPost: (req, res) => {
        let taskId = req.params.id;
        let task = req.body;

        Task.findByIdAndUpdate(taskId, task, {runValidators: true}).then(tasks => {
            res.redirect("/");
        }).catch(err => {
            task.id = taskId;
            task.error = "Cannot edit task.";
            return res.render("task/edit", task);
        });
	}
};