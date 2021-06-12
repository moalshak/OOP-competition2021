# Data YAML files

In this directory are all the configuration files that set the "rules" of the world.
This means that it specifies the following things:

- The different airports
- The different types of planes
- The different types of cargo/trade goods
- The different types of fuel

This directory is meant for the things that should always exist.
If you want to specify some exact parameters of the "simulation", you should do so in the config directory.
This directory is only for things that (almost) never change.

Note that it is very important for the yaml properties to maintain the same name as the corresponding fields in the
Java classes. If this is not the case, FLAPS won't be able to initialize your program correctly.