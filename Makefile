.PHONY: repl run test ancient

repl:
	iced repl -A:dev

run:
	clj -m pod-uochan-tarayo.core

test:
	clojure -R:dev -A:test

outdated:
	clojure -A:outdated

pom:
	clojure -Spom

uberjar: pom
	clojure -A:uberjar

native-image:
	mkdir -p target
	$(GRAALVM_HOME)/bin/native-image \
		-jar target/pod-uochan-tarayo.jar \
		-H:Name=target/pod-uochan-tarayo \
		-H:+ReportExceptionStackTraces \
		-J-Dclojure.spec.skip-macros=true \
		-J-Dclojure.compiler.direct-linking=true \
		"-H:IncludeResources=VERSION" \
		--initialize-at-build-time  \
		--report-unsupported-elements-at-runtime \
		-H:Log=registerResource: \
		--verbose \
		--no-fallback \
		--no-server \
		"-J-Xmx3g"
